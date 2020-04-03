package com.ous.poc.model;

import lombok.Data;

/**
 * @author abdulhafeez
 *
 */
@Data
public class SuspendTaskRequest {
	//optional date time used for suspending a task.
	private String datetime;
}