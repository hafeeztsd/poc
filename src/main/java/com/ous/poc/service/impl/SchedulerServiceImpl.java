package com.ous.poc.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
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
	 * random delay.
	 * 
	 * @return {@link Future} for the scheduled task.
	 */
	@Override
	public int scheduleTask(SchedulableTask schedulableTask) {

		int randomDelay;

		if (schedulableTask.getRandomDelay() == null) {
			randomDelay = random.nextInt((applicationConfig.getMaxDelay() - applicationConfig.getInitialDelay()) + 1)
					+ applicationConfig.getInitialDelay();
		} else {
			randomDelay = schedulableTask.getRandomDelay();
		}

		ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(schedulableTask.getTask(),
				applicationConfig.getInitialDelay(), randomDelay, TimeUnit.SECONDS);

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
