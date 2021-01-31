package com.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springcourse.domain.RequestFile;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.repository.RequestFileRepository;

public class RequestFileService {
	@Autowired
	RequestFileRepository requestFileRepository;

	public PageModel<RequestFile> listAllByRequestId(Long id, PageRequestModel prm) {
		Pageable pageable = PageRequest.of(prm.getPage(), prm.getSize());
		Page<RequestFile> page = requestFileRepository.findAllByRequestId(id, pageable);
		PageModel<RequestFile> pm = new PageModel<RequestFile>(
				(int) page.getTotalElements(),
				page.getSize(),
				page.getTotalPages(),
				page.getContent());
		return pm;
	}
}
