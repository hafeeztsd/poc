package com.ous.poc.model;

public enum TaskPriority {

	NORMAL(1), HIHG(2);

	private int value;

	private TaskPriority(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
