package com.practice.dto;

import com.practice.enums.UserRoles;

import lombok.Data;

@Data
public class UserDto {

	private Long id;
	private String name;
	private String email;
	private UserRoles userRoles;
}
