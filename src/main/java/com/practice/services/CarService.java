package com.practice.services;

import java.io.IOException;
import java.util.List;

import com.practice.dto.BookCarDto;
import com.practice.dto.CarDto;
import com.practice.dto.CarDtoListDto;
import com.practice.dto.SearchCarDto;

public interface CarService {

	public boolean postCar(CarDto carDto) throws IOException;
	
	List<CarDto> getListOfAllCars();
	
	public void deleteCar(Long id);
	
	public CarDto getCarById(Long id);
	
	boolean updateCar(Long carId, CarDto carDto) throws IOException;
	
	boolean BookACar(BookCarDto bookCarDto);
	
	List<BookCarDto> getBookingByUserId(Long userId);
	
	List<BookCarDto> getBooking();
	
	boolean changeBookingStatus(Long bookingId, String status);
	
	CarDtoListDto searchCars(SearchCarDto searchCarDto);
}
