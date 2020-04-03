package com.ous.poc.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author abdulhafeez
 *
 */
@Data
public class CreateTaskRequest {
	@NotNull(message = "Invalid value for required parameter 'title'.")
	private String title;
	@NotNull(message = "Invalid value for required parameter 'description'.")
	private String description;
}