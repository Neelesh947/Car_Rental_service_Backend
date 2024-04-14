package com.practice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.dto.BookCarDto;
import com.practice.dto.CarDto;
import com.practice.dto.SearchCarDto;
import com.practice.services.CarService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CarService carService;
	
	@PostMapping("/create-car")
	public ResponseEntity<?> postCarAdmin(@ModelAttribute CarDto carDto) throws IOException
	{
		boolean success=carService.postCar(carDto);
		if(success)
		{
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@GetMapping("/car-list")
	public ResponseEntity<?> getAllCars()
	{
		return ResponseEntity.ok(carService.getListOfAllCars());
	}
	
	@DeleteMapping("/car/delete/{id}")
	public ResponseEntity<Void> deleteCarById(@PathVariable Long id)
	{
		carService.deleteCar(id);
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/car/{id}")
	public ResponseEntity<CarDto> getCarById(@PathVariable Long id)
	{
		CarDto cardto=carService.getCarById(id);
		return ResponseEntity.status(HttpStatus.OK).body(cardto);
	}
	
	@PutMapping("/car/update/{carId}")
	public ResponseEntity<CarDto> updateCarDetails(@PathVariable Long carId, @ModelAttribute CarDto carDto) throws IOException
	{
		try {
			boolean success=this.carService.updateCar(carId, carDto);
			if(success)
				return ResponseEntity.status(HttpStatus.OK).build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();	
		}
	}
	
	@GetMapping("/car/booking-list")
	public ResponseEntity<List<BookCarDto>> getBookingCars()
	{
		List<BookCarDto> bookingList=carService.getBooking();
		return ResponseEntity.ok(bookingList);
	}
	
	@GetMapping("/car/booking-list/{bookingId}/{status}")
	public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status)
	{
		boolean success=carService.changeBookingStatus(bookingId, status);
		if(success)
			return ResponseEntity.ok().build();
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/car/search")
	public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto)
	{
		return ResponseEntity.ok(carService.searchCars(searchCarDto));
	}
}
