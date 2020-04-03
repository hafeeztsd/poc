package com.ous.poc.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ous.poc.entity.Task;
import com.ous.poc.exception.Error;
import com.ous.poc.exception.ServiceException;
import com.ous.poc.model.CreateTaskRequest;
import com.ous.poc.model.Pageable;
import com.ous.poc.model.SchedulableTask;
import com.ous.poc.model.TaskPriority;
import com.ous.poc.model.TaskResponse;
import com.ous.poc.model.TaskStatus;
import com.ous.poc.model.Tasks;
import com.ous.poc.repository.TaskRepository;
import com.ous.poc.service.SchedulerService;
import com.ous.poc.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link TaskService}. Provide all the CRUD operation related
 * to {@link Task} entity.
 * 
 * @author abdulhafeez
 *
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

	private static final String PRIORITY = "priority";
	private static final String DUE_AT = "dueAt";

	private TaskRepository taskRepository;
	private ModelMapper modelMapper;
	private SchedulerService schedulerService;
	private final List<Sort.Order> sortOrders;

	public TaskServiceImpl(SchedulerService schedulerService, TaskRepository taskRepository, ModelMapper modelMapper) {

		this.schedulerService = schedulerService;
		this.taskRepository = taskRepository;
		this.modelMapper = modelMapper;

		sortOrders = new ArrayList<>();
		sortOrders.add(new Sort.Order(Sort.Direction.ASC, DUE_AT));
		sortOrders.add(new Sort.Order(Sort.Direction.DESC, PRIORITY));
	}

	@Override
	public Tasks findAll(Pageable pageable) {
		Tasks tasks = new Tasks();
		PageRequest pageRequest = PageRequest.of(pageable.getPageNo(), pageable.getPageSize(), Sort.by(sortOrders));
		Page<Task> taskPages = taskRepository.findAll(pageRequest);
		tasks.setTaskList(
				taskPages.stream().map(task -> modelMapper.map(task, TaskResponse.class)).collect(Collectors.toList()));
		tasks.setTotalPages(taskPages.getTotalPages());
		tasks.setTotalRecords(taskPages.getTotalElements());
		return tasks;
	}

	@Override
	public TaskResponse findTask(String taskId) {
		return taskRepository.findById(taskId).map(t -> {
			return modelMapper.map(t, TaskResponse.class);
		}).orElseThrow(() -> new ServiceException(Error.NOT_FOUND, Task.class.getSimpleName()));
	}

	@Override
	public TaskResponse deleteTask(String taskId) {
		return taskRepository.findById(taskId).map(t -> {
			taskRepository.delete(t);
			schedulerService.deleteTask(taskId);
			return modelMapper.map(t, TaskResponse.class);
		}).orElseThrow(() -> new ServiceException(Error.NOT_FOUND, Task.class.getSimpleName()));
	}

	@Override
	public TaskResponse suspendTask(String taskId) {
		return taskRepository.findById(taskId).map(t -> {
			schedulerService.deleteTask(taskId);
			t.setDueAt(null);
			t.setStatus(TaskStatus.SUSPENDED.getValue());
			taskRepository.save(t);
			return modelMapper.map(t, TaskResponse.class);
		}).orElseThrow(() -> new ServiceException(Error.NOT_FOUND, Task.class.getSimpleName()));
	}

	@Override
	public TaskResponse resumeTask(String taskId) {
		return taskRepository.findById(taskId).map(t -> {
			t.setStatus(TaskStatus.PENDING.toString());
			Calendar now = Calendar.getInstance();
			now.add(Calendar.SECOND, t.getDelayInSeconds());
			t.setDueAt(now.getTime());
			Task persistedTask = taskRepository.save(t);
			SchedulableTask schedulableTask = new SchedulableTask(taskId, t.getDelayInSeconds(),
					new TaskExecution(persistedTask));
			schedulerService.scheduleTask(schedulableTask);
			return modelMapper.map(t, TaskResponse.class);
		}).orElseThrow(() -> new ServiceException(Error.NOT_FOUND, Task.class.getSimpleName()));
	}

	@Override
	public TaskResponse createTask(CreateTaskRequest request) {
		Task task = modelMapper.map(request, Task.class);
		task.setPriority(TaskPriority.NORMAL.getValue());
		task.setStatus(TaskStatus.PENDING.getValue());
		task.setId(UUID.randomUUID().toString());
		Task persistedTask = taskRepository.save(task);
		String taskId = persistedTask.getId();
		SchedulableTask schedulableTask = new SchedulableTask(taskId, new TaskExecution(persistedTask));
		int delayInSeconds = schedulerService.scheduleTask(schedulableTask);
		persistedTask.setDelayInSeconds(delayInSeconds);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, task.getDelayInSeconds());
		persistedTask.setDueAt(now.getTime());
		taskRepository.save(persistedTask);
		TaskResponse taskResponse = modelMapper.map(persistedTask, TaskResponse.class);
		return taskResponse;
	}

	/**
	 * Re-Schedule all the tasks, if any, that are available in the database except
	 * suspended ones. We have to do it as current implementation of
	 * {@link SchedulerService} uses {@link ScheduledThreadPoolExecutor}. It is
	 * required in case when server is shutdown and we want to re-associated our non
	 * suspended tasks to the scheduler.
	 * 
	 * @param event
	 */
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {

		String suspendedStatus = TaskStatus.SUSPENDED.getValue();
		List<Task> tasks = taskRepository.findByStatusIsNotLike(suspendedStatus);
		log.info("preparing to schedule {} tasks to startup.", tasks.size());
		tasks.forEach(t -> schedulerService
				.scheduleTask(new SchedulableTask(t.getId(), t.getDelayInSeconds(), new TaskExecution(t))));

	}

	private class TaskExecution implements Runnable {

		private Task task;

		public TaskExecution(Task task) {
			this.task = task;
		}

		@Override
		public void run() {
			Calendar now = Calendar.getInstance();
			task.setResolvedAt(now.getTime());
			task.setStatus(TaskStatus.RUNNING.getValue());
			taskRepository.save(task);

			log.info("Executing {} at {} ", task, LocalTime.now());

			now.add(Calendar.SECOND, task.getDelayInSeconds());
			task.setDueAt(now.getTime());
			task.setStatus(TaskStatus.PENDING.getValue());
			taskRepository.save(task);
		}

	}

}
