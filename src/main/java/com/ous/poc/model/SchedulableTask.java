package com.ous.poc.model;

import lombok.Data;

@Data
public class SchedulableTask {

	private String taskId;
	private Integer randomDelay;
	private Runnable task;

	public SchedulableTask(String taskId, Runnable task) {
		this.taskId = taskId;
		this.task = task;
	}

	public SchedulableTask(String taskId, Integer randomDelay, Runnable task) {
		this(taskId, task);
		this.randomDelay = randomDelay;
	}

}
