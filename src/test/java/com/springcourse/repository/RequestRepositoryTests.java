package com.springcourse.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springcourse.domain.Request;
import com.springcourse.domain.User;
import com.springcourse.domain.enums.RequestState;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class RequestRepositoryTests {
	@Autowired
	private RequestRepository requestRepository;

	//@Test
	public void saveTest() {
		User owner = new User();
		owner.setId(1L);
		Request request = new Request(null,
				"Novo Laptop HP",
				"Pretendo obter um laptop HP",
				new Date(),
				RequestState.OPEN,
				owner, null, null);
		Request createdRequest = requestRepository.save(request);

		assertEquals(createdRequest.getId(), 1L);
	}

	@Test
	public void updateTest() {
		User owner = new User();
		owner.setId(1L);
		Request request = new Request(1L,
				"Novo Laptop HP",
				"Pretendo obter um laptop HP, de RAM 16 GB",
				null,
				RequestState.OPEN,
				owner, null, null);
		Request updateRequest = requestRepository.save(request);

		assertEquals(updateRequest.getDescription(), "Pretendo obter um laptop HP, de RAM 16 GB");
	}

	@Test
	public void getByIdTest() {
		Optional<Request> result = requestRepository.findById(1L);
		Request request = result.get();

		assertEquals(request.getSubject(), "Novo Laptop HP");
	}

	@Test
	public void listTest() {
		List<Request> requests = requestRepository.findAll();
		assertEquals(requests.size(), 1);
	}

	@Test
	public void listByOwnerIdTest() {
		List<Request> requests = requestRepository.findAllByOwnerId(1L);
		assertEquals(requests.size(), 1);
	}

	@Test
	public void updateStateTest() {
		int affectedRows = requestRepository.updateState(1L, RequestState.IN_PROGRESS);
		
		assertEquals(affectedRows, 1);
	}
}
