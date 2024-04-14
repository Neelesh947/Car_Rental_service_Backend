package com.practice.services;

import com.practice.dto.Signuprequest;
import com.practice.dto.UserDto;

public interface UserService {

	UserDto createCustomer(Signuprequest signuprequest);
	
	boolean hasCustomerWithEmail(String email);
}
