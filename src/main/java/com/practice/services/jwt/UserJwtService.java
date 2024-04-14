package com.practice.services.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserJwtService {

	UserDetailsService userDetailsService();
}
