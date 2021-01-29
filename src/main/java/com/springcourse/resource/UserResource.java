package com.springcourse.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.domain.Request;
import com.springcourse.domain.User;
import com.springcourse.dto.UserLoginDTO;
import com.springcourse.dto.UserLoginResponseDTO;
import com.springcourse.dto.UserSaveDTO;
import com.springcourse.dto.UserUpdateDTO;
import com.springcourse.dto.UserUpdateRoleDTO;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.security.JwtManager;
import com.springcourse.service.RequestService;
import com.springcourse.service.UserService;

@RestController
@RequestMapping(value = "users")
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtManager jwtManager;

	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid UserSaveDTO userDTO) {
		User user = userDTO.transformToUser();
		User createdUser = userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(
			@PathVariable("id") Long id,
			@RequestBody @Valid UserUpdateDTO userDTO) {
		User user = userDTO.transformToUser();
		user.setId(id);
		User updatedUser = userService.update(user);
		return ResponseEntity.ok(updatedUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable("id") Long id) {
		User user = userService.getById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping
	public ResponseEntity<PageModel<User>> listAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		PageRequestModel prm = new PageRequestModel(page, size);
		PageModel<User> pm = userService.listAllOnLazyMode(prm);

		return ResponseEntity.ok(pm);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(),
				userLoginDTO.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		org.springframework.security.core.userdetails.User userSpring = (org.springframework.security.core.userdetails.User) authentication
				.getPrincipal();

		String email = userSpring.getUsername();
		List<String> roles = userSpring.getAuthorities()
				.stream()
				.map(authority -> authority.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(jwtManager.createToken(email, roles));
	}

	@GetMapping("/{id}/requests")
	public ResponseEntity<PageModel<Request>> listAllRequestsByOwnerId(
			@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		PageRequestModel prm = new PageRequestModel(page, size);
		PageModel<Request> pm = requestService.listAllByOwnerIdOnLazyMode(id, prm);
		return ResponseEntity.ok(pm);
	}

	@PatchMapping("/role/{id}")
	public ResponseEntity<?> updateRole(@PathVariable("id") Long id,
			@RequestBody @Valid UserUpdateRoleDTO userDTO) {
		User user = new User();
		user.setId(id);
		user.setRole(userDTO.getRole());

		userService.updateRole(user);

		return ResponseEntity.ok().build();
	}

}
