package com.ous.poc.controller;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ous.poc.model.Pageable;
import com.ous.poc.model.CreateTaskRequest;
import com.ous.poc.model.TaskResponse;
import com.ous.poc.model.Tasks;
import com.ous.poc.service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Exposes task management operations to the end user. Entry point of end user
 * calls to the application.
 * 
 * @author abdulhafeez
 *
 */
@Api(value = "Task Management Controller.")
@Validated
@RestController
@RequestMapping("/task")
public class TaskController {

	private TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@ApiOperation(value = "Get Operation. Returns paginagted tasks ordered by due date and priority by default."
			+ " pageNo and pageSize are the only paramters required to use this operation.", response = Tasks.class)

	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Error wile be thrown with 400 as http status code along with a service"
					+ " code to get the more details such as pagNo is missing."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@GetMapping
	public Tasks getAllTasks(
			@RequestParam(required = false, defaultValue = "1") @ApiParam(value = "pageNo", defaultValue = "1") @Valid @Min(1) Integer pageNo,
			@RequestParam @ApiParam(value = "pageSize", defaultValue = "10") @Valid @Min(1) @NotNull Integer pageSize) {
		Pageable pageable = Pageable.builder().pageNo(pageNo).pageSize(pageSize).build();
		return taskService.findAll(pageable);
	}

	@ApiOperation(value = "Get Operation. Returns a task for the provided taskId.", response = TaskResponse.class)

	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Error wile be thrown with 404 as http status code along with a service"
					+ " code that indicates that requested task does not exist in the system."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@GetMapping("/{taskId}")
	public TaskResponse findTask(@PathVariable @ApiParam(value = "taskId") @Valid String taskId) {
		return taskService.findTask(taskId);
	}

	@ApiOperation(value = "Post Operation. Creates and Schedule a new task with the provided detail. "
			+ "Task is scheduled with a random delay. Details can be found in the response.", response = TaskResponse.class)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Error wile be thrown with 400 as http status code along with a service"
					+ " code to get the more details such as required parameter 'title' is missing."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@PostMapping
	public TaskResponse scheduleTask(
			@RequestBody @ApiParam(value = "New Task details") @Valid @NotNull(message = "Request body is missing.") CreateTaskRequest request) {

		return taskService.createTask(request);

	}

	@ApiOperation(value = "Delete Opertion. Deleates a task against given taskId", response = TaskResponse.class)

	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Error wile be thrown with 404 as http status code along with a service"
					+ " code that indicates that requested task does not exist in the system."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@DeleteMapping("/{taskId}")
	public TaskResponse deleteTask(@PathVariable @ApiParam(value = "taskId") @Valid UUID taskId) {
		return taskService.deleteTask(taskId.toString());
	}

	@ApiOperation(value = "PUT Operation.Suspend a task aginst given taskId. This task remain in the system with Suspended state "
			+ "and further executions are get deferred.", response = TaskResponse.class)

	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Error wile be thrown with 404 as http status code along with a service"
					+ " code that indicates that requested task does not exist in the system."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@PutMapping("/{taskId}/suspend")
	public TaskResponse suspendTask(@PathVariable @ApiParam(value = "taskId") @Valid String taskId) {
		return taskService.suspendTask(taskId);
	}

	@ApiOperation(value = "PUT operation. Resume a task against given taskId. Task will no more be in SUSPENDED state after it.", response = TaskResponse.class)

	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Task List fetched successfully"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Error wile be thrown with 404 as http status code along with a service"
					+ " code that indicates that requested task does not exist in the system."),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error Code 500 will be thrown to indicate something wrong with the system.") })

	@PutMapping("/{taskId}/resume")
	public TaskResponse resumeTask(@PathVariable @ApiParam(value = "taskId") @Valid String taskId) {
		return taskService.resumeTask(taskId);
	}

}
