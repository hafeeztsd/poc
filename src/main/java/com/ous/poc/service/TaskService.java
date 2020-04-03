package com.ous.poc.service;

import com.ous.poc.exception.ServiceException;
import com.ous.poc.model.CreateTaskRequest;
import com.ous.poc.model.Pageable;
import com.ous.poc.model.TaskResponse;
import com.ous.poc.model.Tasks;

/**
 * 
 * @author abdulhafeez
 *
 */
public interface TaskService {

	/**
	 * Creates and schedule the task in the system.
	 * 
	 * @param request
	 * @return newly created task
	 * @throws @{@link
	 * 			ServiceException} in case something goes wrong while creating a
	 *             new task.
	 */
	TaskResponse createTask(CreateTaskRequest request);

	/**
	 * 
	 * @param pageable
	 * @return list of paginated and sorted tasks
	 * @throws @{@link
	 * 			ServiceException} in case something goes wrong while creating a
	 *             new task.
	 */
	Tasks findAll(Pageable pageable);

	/**
	 * 
	 * @param taskId
	 * @return {@link TaskResponse} identified by provided taskId.
	 * @throws @{@link
	 * 			ServiceException} in case task does not exist in the system.
	 */
	TaskResponse findTask(String taskId);

	/**
	 * 
	 * @param taskId
	 * @return deleted task identified by provided taskId.
	 * @throws @{@link
	 * 			ServiceException} in case task does not exist in the system.
	 */
	TaskResponse deleteTask(String taskId);

	/**
	 * 
	 * @param taskId
	 * @return suspend task identified by provided taskId.
	 * @throws @{@link
	 * 			ServiceException} in case task does not exist in the system.
	 */
	TaskResponse suspendTask(String taskId);

	/**
	 * 
	 * @param taskId
	 * @return resume task identified by provided taskId.
	 * @throws @{@link
	 * 			ServiceException} in case task does not exist in the system.
	 */
	TaskResponse resumeTask(String taskId);

}
