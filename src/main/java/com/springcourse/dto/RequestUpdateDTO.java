package com.springcourse.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.springcourse.domain.Request;
import com.springcourse.domain.RequestFile;
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
public class RequestUpdateDTO {
	@NotBlank(message = "Subject required")
	private String subject;
	
	private String description;
	
	@NotNull(message = "State required")
	private RequestState state;

	@NotNull(message = "Owner required")
	private User owner;
	
	private List<RequestStage> stages = new ArrayList<RequestStage>();
	private List<RequestFile> files = new ArrayList<RequestFile>();

	public Request transformToRequest() {
		Request request = new Request(null, subject, description, null, state, owner, stages, files);
		return request;
	}
}
