package com.practice.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.practice.dto.Signuprequest;
import com.practice.dto.UserDto;
import com.practice.entity.User;
import com.practice.enums.UserRoles;
import com.practice.repository.UserRepository;
import com.practice.services.UserService;

import jakarta.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	
	@PostConstruct
	public void createAdminAccount()
	{
		User adminAccount=userRepository.findByUserRoles(UserRoles.ADMIN);
		if(adminAccount==null)
		{
			User newAdminAccount=new User();
			newAdminAccount.setName("Admin");
			newAdminAccount.setEmail("admin@gmail.com");
			newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
			newAdminAccount.setUserRoles(UserRoles.ADMIN);
			userRepository.save(newAdminAccount);
			System.out.println("Admin Acoount is created successfully");
		}
	}
	
	@Override
	public UserDto createCustomer(Signuprequest signuprequest) {
		// TODO Auto-generated method stub
		User user=new User();
		user.setName(signuprequest.getName());
		user.setEmail(signuprequest.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(signuprequest.getPassword()));
		user.setUserRoles(UserRoles.CUSTOMER);
		User createdUser=userRepository.save(user);
		UserDto userDto=new UserDto();
		userDto.setId(createdUser.getId());	
		userDto.setEmail(createdUser.getEmail());
		userDto.setName(createdUser.getName());
		userDto.setUserRoles(createdUser.getUserRoles());
		return userDto;
	}

	@Override
	public boolean hasCustomerWithEmail(String email) {
		// TODO Auto-generated method stub		
		return userRepository.findFirstByEmail(email).isPresent();
	}

}
