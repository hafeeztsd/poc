package com.ous.poc.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.ous.poc.config.ApplicationConfig;
import com.ous.poc.model.SchedulableTask;
import com.ous.poc.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Task scheduler that internally uses {@link ScheduledThreadPoolExecutor} for
 * scheduling the provided task with random delay.
 * 
 * @author abdulhafeez
 *
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

	private ScheduledThreadPoolExecutor scheduledExecutorService;
	private Random random;
	private Map<String, ScheduledFuture<?>> taskFutureMap;
	private ApplicationConfig applicationConfig;

	public SchedulerServiceImpl(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
		scheduledExecutorService = new ScheduledThreadPoolExecutor(applicationConfig.getPoolSize());
		scheduledExecutorService.setRemoveOnCancelPolicy(true);
		taskFutureMap = new HashMap<>();
		random = new Random();
	}

	/**
	 * Schedule the given task with provided details along with an initial and
	 * random delay. <br>
	 * 
	 * Random delay will not be calculated for all those tasks that are suspended
	 * and user want to resume them. Same random delay will be used when the were
	 * suspended. <br>
	 * 
	 * In most of the cases the initial configured delay, app.config.initial-delay,
	 * is used. However if user wants to postpone a task for some future date than
	 * initial delay would be calculated based on following formula instead of
	 * taking it from the configuration. <br>
	 * 
	 * initialDelay = schedulableTask.getPosponeDate().getTime() - System.currentTimeMillis(); 
	 * 
	 * @return the random delay associated with this task.
	 */
	@Override
	public int scheduleTask(SchedulableTask schedulableTask) {

		int randomDelay;
		long initialDelay = applicationConfig.getInitialDelay();

		if (schedulableTask.getRandomDelay() == null) {
			randomDelay = random.nextInt((applicationConfig.getMaxDelay() - applicationConfig.getInitialDelay()) + 1)
					+ applicationConfig.getInitialDelay();
		} else {
			randomDelay = schedulableTask.getRandomDelay();
		}

		if (schedulableTask.getPosponeDate() != null) {
			initialDelay = (schedulableTask.getPosponeDate().getTime() - System.currentTimeMillis())/1000;
		}

		ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(schedulableTask.getTask(),
				initialDelay, randomDelay, TimeUnit.SECONDS);

		log.info("task {} is scheduled with {} delay ", schedulableTask.getTaskId(), randomDelay);

		taskFutureMap.put(schedulableTask.getTaskId(), future);

		return randomDelay;
	}

	/**
	 * Deletes the given task identified by taskId.
	 * 
	 * @return {@link Boolean} true in case task is deleted successfully.
	 * 
	 */
	@Override
	public boolean deleteTask(String taskId) {
		ScheduledFuture<?> future = taskFutureMap.remove(taskId);
		boolean deleted = false;
		if (future != null) {
			deleted = future.cancel(true);
		}
		return deleted;
	}

}
