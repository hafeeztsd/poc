package com.ous.poc.model;

import java.util.List;

import lombok.Data;

@Data
public class Tasks {

	private List<TaskResponse> taskList;
	private long totalRecords;
	private int totalPages;

}
