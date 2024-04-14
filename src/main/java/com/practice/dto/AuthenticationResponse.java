package com.practice.dto;

import com.practice.enums.UserRoles;

import lombok.Data;

@Data
public class AuthenticationResponse {

	private String jwt;
	
	private UserRoles  userRoles;
	
	private Long userId;
}
