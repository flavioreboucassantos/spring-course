package com.springcourse.resource.exception;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiErrorField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String fieldError;

}
