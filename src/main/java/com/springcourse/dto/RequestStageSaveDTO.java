package com.springcourse.dto;

import javax.validation.constraints.NotNull;

import com.springcourse.domain.Request;
import com.springcourse.domain.RequestStage;
import com.springcourse.domain.User;
import com.springcourse.domain.enums.RequestState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestStageSaveDTO {
	private String description;
	
	@NotNull(message="State required")
	private RequestState state;
	
	@NotNull(message="Request required")
	private Request request;
	
	@NotNull(message="Owner required")
	private User owner;

	public RequestStage transformToRequestStage() {
		RequestStage requestStage = new RequestStage(null, description, null, state, request, owner);
		return requestStage;
	}
}
