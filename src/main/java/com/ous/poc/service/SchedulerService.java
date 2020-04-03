package com.ous.poc.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.ous.poc.model.SchedulableTask;

/**
 * Task scheduler that internally uses {@link ScheduledThreadPoolExecutor} for
 * scheduling the provided task with random delay.
 * 
 * @author abdulhafeez
 *
 */
public interface SchedulerService {

	/**
	 * schedule task with provided details.
	 * 
	 * @param schedulableTask
	 * @return fixed delay after which task will be executed.
	 */
	int scheduleTask(SchedulableTask schedulableTask);

	/**
	 * Delete task identified by taskId.
	 * 
	 * @param taskId
	 * @return
	 */
	boolean deleteTask(String taskId);

}
