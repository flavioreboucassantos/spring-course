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
import com.springcourse.domain.RequestStage;
import com.springcourse.domain.User;
import com.springcourse.domain.enums.RequestState;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class RequestStageRepositoryTest {
	@Autowired
	private RequestStageRepository requestStageRepository;

	// @Test
	public void saveTest() {
		User owner = new User();
		owner.setId(1L);

		Request request = new Request();
		request.setId(1L);

		RequestStage stage = new RequestStage(null,
				"Foi comprado um novo laptop de marca HP e com 16 GB de RAM",
				new Date(),
				RequestState.CLOSED,
				request,
				owner);
		RequestStage createdStage = requestStageRepository.save(stage);

		assertEquals(createdStage.getId(), 1L);
	}

	@Test
	public void getByIdTest() {
		Optional<RequestStage> result = requestStageRepository.findById(1L);
		RequestStage stage = result.get();

		assertEquals(stage.getDescription(), "Foi comprado um novo laptop de marca HP e com 16 GB de RAM");
	}

	@Test
	public void listByRequestIdTest() {
		List<RequestStage> stages = requestStageRepository.findAllByRequestId(1L);

		assertEquals(stages.size(), 1);
	}

}
