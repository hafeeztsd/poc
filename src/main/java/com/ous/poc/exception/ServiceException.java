package com.ous.poc.exception;

import com.ous.poc.service.impl.TaskServiceImpl;

/**
 * Exception class that will be thrown by the service layer incase something
 * goes wrong. Check {@link TaskServiceImpl} for more details.
 * 
 * @author abdulhafeez
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 7039722475612709090L;
	private Error error;
	String[] args;

	public ServiceException(Error error, String... args) {
		this.error = error;
		this.args = args;
	}

	public Error getError() {
		return error;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
