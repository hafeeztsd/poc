package com.ous.poc.exception;

import org.springframework.http.HttpStatus;

/**
 * Mapping of HTTP and Service error codes. 
 * Client can get more detail with the help of the detailed Error object. 
 * 
 * @author abdulhafeez
 *
 */
public enum Error {
	/**
	 * Error Codes and messages
	 */
	UNEXPECTED_ERROR(1000, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error."), 
	ALREADY_EXISTS(1001,HttpStatus.BAD_REQUEST, "%s already exists."), 
	INVALID_PARAMETER(1002, HttpStatus.BAD_REQUEST,"Invalid parameter."), 
	NOT_FOUND(1003, HttpStatus.NOT_FOUND, "%s not exists."), 
	DATABASE_ERROR(1006,HttpStatus.BAD_REQUEST, "Database Error."),
	INVALID_VALUE(1004, HttpStatus.BAD_REQUEST,"Invalid value for %s"), 
	VALIDATION_ERROR(1005, HttpStatus.BAD_REQUEST,"Validation error."), 
	INVALID_DATE(1017, HttpStatus.BAD_REQUEST,"Invalid Date formate dd/MM/yyyy.");

	private Integer code;
	private String message;
	private HttpStatus httpStatus;

	Error(Integer code, HttpStatus httpStatus, String message) {
		this.code = code;
		this.httpStatus = httpStatus;

		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}

}