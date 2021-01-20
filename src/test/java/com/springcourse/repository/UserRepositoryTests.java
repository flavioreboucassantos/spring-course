package com.springcourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springcourse.domain.User;
import com.springcourse.domain.enums.Role;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void saveTest() {
		User user = new User(null,
				"Flávio",
				"FlavioReboucasSantos@gmail.com",
				"123456",
				Role.ADMINISTRATOR,
				null,
				null);
		User createdUser = userRepository.save(user);

		assertThat(createdUser.getId()).isEqualTo(1L);
	}

	@Test
	public void updateTest() {
		User user = new User(1L,
				"Flávio Rebouças Santos",
				"FlavioReboucasSantos@gmail.com",
				"123456",
				Role.ADMINISTRATOR,
				null,
				null);
		User updateUser = userRepository.save(user);

		assertThat(updateUser.getName()).isEqualTo("Flávio Rebouças Santos");
	}

	@Test
	public void getByIdTest() {
		Optional<User> result = userRepository.findById(1L);
		User user = result.get();

		assertThat(user.getPassword()).isEqualTo("123456");
	}

	@Test
	public void listTest() {
		List<User> users = userRepository.findAll();

		assertThat(users.size()).isEqualTo(1);
	}

	@Test
	public void loginTest() {
		Optional<User> result = userRepository.login("FlavioReboucasSantos@gmail.com", "123456");
		User loggedUser = result.get();

		assertThat(loggedUser.getId()).isEqualTo(1L);
	}
}
