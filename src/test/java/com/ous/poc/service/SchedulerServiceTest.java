package com.ous.poc.service;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ous.poc.Application;
import com.ous.poc.model.SchedulableTask;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit test for {@link SchedulerService}
 * 
 * @author abdulhafeez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@Slf4j
public class SchedulerServiceTest {

	@Autowired
	private SchedulerService schedulerService;

	@Test
	public void shouldScheduleTask() {
		String taskId = UUID.randomUUID().toString();
		Runnable task = () -> log.info("Executing Task {} ", taskId);
		SchedulableTask schedulableTask = new SchedulableTask(taskId, task);
		int delay = schedulerService.scheduleTask(schedulableTask);
		Assert.assertTrue("Task scheduling failed. ", delay > 0);
	}

	@Test
	public void shouldDeleteTaskTest() {
		String taskId = UUID.randomUUID().toString();
		Runnable task = () -> log.info("Executing Task {} ", taskId);
		SchedulableTask schedulableTask = new SchedulableTask(taskId, task);
		schedulerService.scheduleTask(schedulableTask);
		boolean deleted = schedulerService.deleteTask(taskId);
		Assert.assertTrue("Task deletion failed. ", deleted);
	}

}
