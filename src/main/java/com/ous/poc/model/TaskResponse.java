package com.ous.poc.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class TaskResponse implements Response, Serializable {

	private static final long serialVersionUID = -6456365346031631140L;
	private String id;
	private String title;
	private String description;
	private String status;
	private String priority;
	private int delayInSeconds;
	private Date dueAt;
	private Date resolvedAt;
	private Date createdAt;
	private Date updatedAt;

}
