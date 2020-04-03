package com.ous.poc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ous.poc.Application;
import com.ous.poc.exception.ServiceException;
import com.ous.poc.model.Pageable;
import com.ous.poc.model.CreateTaskRequest;
import com.ous.poc.model.TaskResponse;
import com.ous.poc.model.TaskStatus;
import com.ous.poc.model.Tasks;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit tests for {@link TaskService}
 * 
 * @author abdulhafeez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@Slf4j
public class TaskServiceTest {

	@Autowired
	private TaskService taskService;

	@Test
	public void shouldCreateTaskTest() {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setDescription("Echo Task");
		createTaskRequest.setTitle("Echo Task");
		log.info("preparing to create task {} ", createTaskRequest);
		TaskResponse taskResponse = taskService.createTask(createTaskRequest);
		log.info("Newly added task: {} ", taskResponse);
		Assert.assertNotNull("Invalid task id", taskResponse.getId());
	}

	@Test
	public void shouldCreateAndFetchPaginatedTaskListTest() {

		// creating 50 tasks.
		for (int i = 0; i < 50; i++) {
			CreateTaskRequest createTaskRequest = new CreateTaskRequest();
			createTaskRequest.setDescription("Test Task#" + i);
			createTaskRequest.setTitle("Test Task#" + i);
			taskService.createTask(createTaskRequest);
		}

		Tasks tasks = taskService.findAll(Pageable.builder().pageNo(1).pageSize(10).build());
		Assert.assertNotNull("tasks respone is null", tasks);
		Assert.assertNotNull("no task retuned", tasks.getTaskList());
		Assert.assertEquals("task per page count mismatched", 10, tasks.getTaskList().size());
		Assert.assertTrue("total task count mismatched", tasks.getTotalRecords() >= 50);
		Assert.assertTrue("number of pages count mismatched", tasks.getTotalPages() >= 5);

	}

	@Test(expected = ServiceException.class)
	public void shouldDeleteTaskTest() {

		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setDescription("Test Task");
		createTaskRequest.setTitle("Test Task");
		TaskResponse task = taskService.createTask(createTaskRequest);
		taskService.deleteTask(task.getId().toString());
		taskService.findTask(task.getId().toString());
	}

	@Test
	public void shouldSuspendTaskTest() {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setDescription("Test Task");
		createTaskRequest.setTitle("Test Task");
		TaskResponse task = taskService.createTask(createTaskRequest);
		task = taskService.suspendTask(task.getId().toString());
		Assert.assertEquals("Task is NOT in Suspended state", TaskStatus.SUSPENDED.getValue(), task.getStatus());
	}

	@Test
	public void shouldResumeTaskTest() {
		CreateTaskRequest createTaskRequest = new CreateTaskRequest();
		createTaskRequest.setDescription("Test Task");
		createTaskRequest.setTitle("Test Task");
		TaskResponse task = taskService.createTask(createTaskRequest);
		task = taskService.suspendTask(task.getId().toString());
		task = taskService.resumeTask(task.getId().toString());
		Assert.assertNotEquals("Task is still in Suspended state", TaskStatus.SUSPENDED.getValue(), task.getStatus());

	}
}
