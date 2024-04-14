package com.practice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.dto.BookCarDto;
import com.practice.dto.CarDto;
import com.practice.services.CarService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CarService carService;
	
	@GetMapping("/car-list")
	public ResponseEntity<?> getAllCars()
	{
		return ResponseEntity.ok(carService.getListOfAllCars());
	}
	
	@PostMapping("/car/book")
	public ResponseEntity<Void> bookACAR(@RequestBody BookCarDto bookCarDto)
	{
		boolean success= carService.BookACar(bookCarDto);
		
		if(success)
		{
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@GetMapping("/car/{id}")
	public ResponseEntity<CarDto> getCarById(@PathVariable Long id)
	{
		CarDto cardto=carService.getCarById(id);
		if(cardto==null) return ResponseEntity.notFound().build();
		return ResponseEntity.status(HttpStatus.OK).body(cardto);
	}
	
	@GetMapping("/car/booking/{userId}")
	public ResponseEntity<List<BookCarDto>> getBookingByUserId(@PathVariable Long userId)
	{
		List<BookCarDto> booklist=carService.getBookingByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(booklist);
	}
}
