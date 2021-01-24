package com.springcourse.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.domain.Request;
import com.springcourse.domain.RequestStage;
import com.springcourse.domain.User;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.service.RequestService;
import com.springcourse.service.RequestStageService;

@RestController
@RequestMapping(value = "requests")
public class RequestResource {
	@Autowired
	private RequestService requestService;

	@Autowired
	private RequestStageService requestStageService;

	@PostMapping
	public ResponseEntity<Request> save(@RequestBody Request request) {
		Request createdRequest = requestService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Request> update(@PathVariable("id") Long id, @RequestBody Request request) {
		request.setId(id);
		Request updatedRequest = requestService.update(request);
		return ResponseEntity.ok(updatedRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Request> getById(@PathVariable("id") Long id) {
		Request request = requestService.getById(id);
		return ResponseEntity.ok(request);
	}

	@GetMapping
	public ResponseEntity<PageModel<Request>> listAll(
			@RequestParam("page") int page,
			@RequestParam("size") int size) {
		PageRequestModel prm = new PageRequestModel(page, size);
		PageModel<Request> pm = requestService.listAllOnLazyMode(prm);

		return ResponseEntity.ok(pm);
	}

	@GetMapping("/{id}/request-stages")
	public ResponseEntity<PageModel<RequestStage>> listAllRequestStagesByRequestId(
			@PathVariable("id") Long id,
			@RequestParam("page") int page,
			@RequestParam("size") int size) {
		PageRequestModel prm = new PageRequestModel(page, size);
		PageModel<RequestStage> pm = requestStageService.listAllByRequestIdOnLazyMode(id, prm);
		
		return ResponseEntity.ok(pm);
	}
}
