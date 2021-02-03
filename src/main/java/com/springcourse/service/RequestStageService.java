package com.springcourse.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springcourse.domain.RequestStage;
import com.springcourse.exception.NotFoundException;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.repository.RequestRepository;
import com.springcourse.repository.RequestStageRepository;

@Service
public class RequestStageService {
	@Autowired
	private RequestStageRepository requestStageRepository;

	@Autowired
	private RequestRepository requestRepository;

	public RequestStage save(RequestStage requestStage) {
		requestStage.setRealizationDate(new Date());

		RequestStage createdRequestStage = requestStageRepository.save(requestStage);

		requestRepository.updateState(requestStage.getRequest().getId(), requestStage.getState());

		return createdRequestStage;
	}

	public RequestStage getById(Long id) {
		Optional<RequestStage> result = requestStageRepository.findById(id);
		return result.orElseThrow(() -> new NotFoundException("There are not request stage with id = " + id));
	}

	public List<RequestStage> listAllByRequestId(Long requestId) {
		List<RequestStage> requestStages = requestStageRepository.findAllByRequestId(requestId);
		return requestStages;
	}

	public PageModel<RequestStage> listAllByRequestIdOnLazyMode(Long id, PageRequestModel prm) {
		Pageable pageable = prm.toStringPageRequest();
		Page<RequestStage> page = requestStageRepository.findAllByRequestId(id, pageable);
		PageModel<RequestStage> pm = new PageModel<RequestStage>(
				(int) page.getTotalElements(),
				page.getSize(),
				page.getTotalPages(),
				page.getContent());
		return pm;
	}
}
