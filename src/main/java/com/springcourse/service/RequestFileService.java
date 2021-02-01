package com.springcourse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springcourse.domain.Request;
import com.springcourse.domain.RequestFile;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.model.UploadedFileModel;
import com.springcourse.repository.RequestFileRepository;
import com.springcourse.service.s3.S3Service;

@Service
public class RequestFileService {

	@Autowired
	private RequestFileRepository requestFileRepository;

	@Autowired
	private S3Service s3Service;

	public List<RequestFile> upload(Long requestId, MultipartFile[] files) {
		List<UploadedFileModel> uploadedFiles = s3Service.upload(files);
		List<RequestFile> requestFiles = new ArrayList<RequestFile>();

		uploadedFiles.forEach(uploadedFile -> {
			RequestFile requestFile = new RequestFile();
			requestFile.setName(uploadedFile.getName());
			requestFile.setLocation(uploadedFile.getLocation());

			Request request = new Request();
			request.setId(requestId);

			requestFile.setRequest(request);

			requestFiles.add(requestFile);
		});

		return requestFileRepository.saveAll(requestFiles);
	}

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
