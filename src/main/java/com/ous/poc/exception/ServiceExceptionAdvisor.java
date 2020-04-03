package com.ous.poc.exception;

import java.sql.SQLException;
import java.util.concurrent.CompletionException;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller advice to wrap the error object into a consistent format
 * {@link ErrorResponse} whenever error occured in the system.
 * 
 * @author abdulhafeez
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ServiceExceptionAdvisor extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return responseEntity(new ErrorResponse(Error.INVALID_PARAMETER, ex, "Malformed JSON"));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(Error.VALIDATION_ERROR);
		errorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
		errorResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String errorMessage = ex.getParameterName() + " parameter is missing";
		logger.error(ex);
		return responseEntity(new ErrorResponse(Error.INVALID_PARAMETER, ex, errorMessage));
	}

	@ExceptionHandler(ServiceException.class)
	protected ResponseEntity<Object> handleServiceException(ServiceException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getError(), ex,
				String.format(ex.getError().getMessage(), (Object[]) ex.getArgs()));
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse(Error.VALIDATION_ERROR);
		errorResponse.addValidationError(ex.getConstraintViolations());
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	@ExceptionHandler(SQLException.class)
	protected ResponseEntity<Object> handleDataBaseExceptions(SQLException ex) {
		ErrorResponse errorResponse = new ErrorResponse(Error.DATABASE_ERROR, ex);
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(Error.UNEXPECTED_ERROR, ex);
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	/**
	 * unboxing for CompletionException.
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(CompletionException.class)
	protected ResponseEntity<Object> handleCompletionException(CompletionException ex) {
		logger.error(ex);
		if (ex.getCause() instanceof ServiceException) {
			return handleServiceException((ServiceException) ex.getCause());
		} else if (ex.getCause() instanceof SQLException) {
			return handleDataBaseExceptions((SQLException) ex.getCause());
		} else if (ex.getCause() instanceof ConstraintViolationException) {
			return handleConstraintViolationException((ConstraintViolationException) ex.getCause());
		} else if (ex.getCause() instanceof MethodArgumentNotValidException) {
			return handleMethodArgumentNotValid((MethodArgumentNotValidException) ex.getCause(), null, null, null);
		}

		ErrorResponse errorResponse = new ErrorResponse(Error.UNEXPECTED_ERROR, ex);

		if (ex.getCause().getLocalizedMessage() != null) {
			errorResponse.setDebugMessage(ex.getCause().getLocalizedMessage());
		} else {
			errorResponse.setDebugMessage(ex.getLocalizedMessage());
		}
		errorResponse.setDebugMessage(ex.getCause().getLocalizedMessage());
		return responseEntity(errorResponse);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest webRequest) {
		ErrorResponse errorResponse = new ErrorResponse(Error.INVALID_PARAMETER);
		String error = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		errorResponse.setMessage(error);
		errorResponse.setDebugMessage(ex.getMessage());
		logger.error(ex);
		return responseEntity(errorResponse);
	}

	private ResponseEntity<Object> responseEntity(ErrorResponse errorResponse) {
		System.out.println("===========");
		System.out.println("error response: " + errorResponse);
		System.out.println(
				"error response message : " + errorResponse.getMessage() + " code: " + errorResponse.getCode());

		System.out.println("===========");

		return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
	}

}
