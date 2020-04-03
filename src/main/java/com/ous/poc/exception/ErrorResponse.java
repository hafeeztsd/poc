package com.ous.poc.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.ous.poc.model.Response;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Client can get this object as a response in case something is wrong. It could
 * be because of the provided request or system failure.
 * 
 * @author abdulhafeez
 *
 */
@Data
public class ErrorResponse implements Response {
	private int code;
	private HttpStatus httpStatus;
	private String message;
	private String debugMessage;
	private List<BaseError> subErrors;
	private final LocalDateTime time;

	private ErrorResponse() {
		this.time = LocalDateTime.now();
	}

	public ErrorResponse(Error error) {
		this();
		this.code = error.getCode();
		this.httpStatus = error.getHttpStatus();
		this.message = error.getMessage();
	}

	public ErrorResponse(Error error, String message) {
		this(error);
		this.message = message;
	}

	public ErrorResponse(Error error, Throwable ex) {
		this(error);
		this.debugMessage = ex.getLocalizedMessage();
	}

	public ErrorResponse(Error error, Throwable ex, String message) {
		this(error, message);
		this.debugMessage = ex.getLocalizedMessage();
	}

	public void addValidationError(String objectName, String errorMessage, String key, Object value) {
		addSubError(new ValidationError(objectName, errorMessage, key, value));
	}

	public void addValidationError(String objectName, String message) {
		addSubError(new ValidationError(objectName, message));
	}

	public void addValidationError(FieldError fieldError) {
		addValidationError(fieldError.getObjectName(), fieldError.getDefaultMessage(), fieldError.getField(),
				fieldError.getRejectedValue());
	}

	public void addValidationErrors(List<FieldError> fieldError) {
		fieldError.forEach(this::addValidationError);
	}

	public void addValidationError(ObjectError objectError) {
		addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
	}

	public void addValidationError(List<ObjectError> objectErrors) {
		objectErrors.forEach(this::addValidationError);
	}

	public void addValidationError(ConstraintViolation<?> constraintViolation) {
		addValidationError(constraintViolation.getRootBeanClass().getSimpleName(), constraintViolation.getMessage(),
				((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().asString(),
				constraintViolation.getInvalidValue());

	}

	public void addValidationError(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addValidationError);

	}

	private void addSubError(BaseError error) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(error);
	}

	abstract class BaseError {
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	@AllArgsConstructor
	class ValidationError extends BaseError {
		private final String objectName;
		private final String errorMessage;
		private String key;
		private Object value;

		public ValidationError(String objectName, String errorMessage) {
			this.objectName = objectName;
			this.errorMessage = errorMessage;
		}
	}
}
