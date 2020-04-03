package com.ous.poc.model;

public enum TaskStatus {

	PENDING("Pending"), RUNNING("Running"), SUSPENDED("Suspended");

	private String value;

	private TaskStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
