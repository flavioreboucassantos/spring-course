package com.springcourse.resource.exception;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorList extends ApiError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ApiErrorField> errors;

	public ApiErrorList(int code, String msg, Date date, List<ApiErrorField> errors) {
		super(code, msg, date);
		this.errors = errors;
	}

}
