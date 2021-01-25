package com.springcourse.dto;

import com.springcourse.domain.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRoleDTO {
	private Role role;
}
