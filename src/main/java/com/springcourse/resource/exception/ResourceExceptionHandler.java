package com.springcourse.resource.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.springcourse.exception.NotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handle(NotFoundException e) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handle(BadCredentialsException e) {
		ApiError error = new ApiError(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handle(AccessDeniedException e) {
		ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), e.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
	
	@ExceptionHandler(SizeLimitExceededException.class)
	public ResponseEntity<ApiError> handle(SizeLimitExceededException e) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ApiErrorField> errors = new ArrayList<ApiErrorField>();
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			ApiErrorField errorField = new ApiErrorField(fieldError.getField(), fieldError.getDefaultMessage());
			errors.add(errorField);
		});

		String defaultMessage = "Invalid field(s)";
		
		ApiErrorList error = new ApiErrorList(HttpStatus.BAD_REQUEST.value(), defaultMessage, new Date(), errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
