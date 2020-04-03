package com.ous.poc.model;

import java.util.Date;

import lombok.Data;

@Data
public class SchedulableTask {

	private String taskId;
	private Integer randomDelay;
	private Date posponeDate;
	private Runnable task;

	public SchedulableTask(String taskId, Runnable task) {
		this.taskId = taskId;
		this.task = task;
	}

	public SchedulableTask(String taskId, Integer randomDelay, Runnable task) {
		this(taskId, task);
		this.randomDelay = randomDelay;
	}

	public SchedulableTask(String taskId, Integer randomDelay, Date posponeDate, Runnable task) {
		this(taskId, task);
		this.randomDelay = randomDelay;
		this.posponeDate = posponeDate;
	}

}
