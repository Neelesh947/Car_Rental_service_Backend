package com.practice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.dto.AuthenticationRequest;
import com.practice.dto.AuthenticationResponse;
import com.practice.dto.Signuprequest;
import com.practice.dto.UserDto;
import com.practice.entity.User;
import com.practice.repository.UserRepository;
import com.practice.services.UserService;
import com.practice.services.jwt.UserJwtService;
import com.practice.utils.JWTUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService  userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserJwtService userJwtService;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("create-user")
	public ResponseEntity<?> signUprequest(@RequestBody Signuprequest signuprequest)
	{
		if(userService.hasCustomerWithEmail(signuprequest.getEmail()))
		{
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email already exist");
		}		
		UserDto user= userService.createCustomer(signuprequest);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something Went Wrong");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	@PostMapping("login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
				BadCredentialsException, DisabledException, UsernameNotFoundException
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException e)
		{
			throw new BadCredentialsException("Incorrect Username or password");
		}
		
		final UserDetails userDetails=userJwtService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());
		
		final String jwt=jwtUtils.generateToken(userDetails);
		AuthenticationResponse authenticationResponse=new AuthenticationResponse();
		if(optionalUser.isPresent())
		{
			authenticationResponse.setJwt(jwt);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setUserRoles(optionalUser.get().getUserRoles()); 
		}
		
		return authenticationResponse;
	}
}
