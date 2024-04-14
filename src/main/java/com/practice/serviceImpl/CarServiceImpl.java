package com.practice.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.practice.dto.BookCarDto;
import com.practice.dto.CarDto;
import com.practice.dto.CarDtoListDto;
import com.practice.dto.SearchCarDto;
import com.practice.entity.Car;
import com.practice.entity.User;
import com.practice.enums.BookCarStatus;
import com.practice.repository.BookACarRepository;
import com.practice.repository.CarRepository;
import com.practice.repository.UserRepository;
import com.practice.services.CarService;
import com.practice.entity.BookACar;

@Service
public class CarServiceImpl implements CarService{

	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookACarRepository bookACarRepository;
	
	public boolean postCar(CarDto carDto) throws IOException
	{
		try {
			Car car=new Car();
			car.setName(carDto.getName());
			car.setBrand(carDto.getBrand());
			car.setColor(carDto.getColor());
			car.setPrice(carDto.getPrice());
			car.setYear(carDto.getYear());
			car.setType(carDto.getType());
			car.setDescription(carDto.getDescription());
			car.setTransmission(carDto.getTransmission());
			car.setImage(carDto.getImage().getBytes());
			carRepository.save(car);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}

	@Override
	public List<CarDto> getListOfAllCars() {
		// TODO Auto-generated method stub
		return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
	}

	@Override
	public void deleteCar(Long id) {
		// TODO Auto-generated method stub
		carRepository.deleteById(id);		
	}

	@Override
	public CarDto getCarById(Long id) {
		// TODO Auto-generated method stub
		Optional<Car> optionalCar=carRepository.findById(id);
		return optionalCar.map(Car::getCarDto).orElse(null);
	}

	@Override
	public boolean updateCar(Long carId, CarDto carDto) throws IOException {
		// TODO Auto-generated method stub
		Optional<Car> optionalCar=carRepository.findById(carId);
		if(optionalCar.isPresent())
		{
			Car existingCar=new Car();
			if(carDto.getImage()!=null)
			{
				existingCar.setImage(carDto.getImage().getBytes());
			}
			existingCar.setPrice(carDto.getPrice());
			existingCar.setBrand(carDto.getBrand());
			existingCar.setColor(carDto.getColor());
			existingCar.setDescription(carDto.getDescription());
			existingCar.setName(carDto.getName());
			existingCar.setTransmission(carDto.getTransmission());
			existingCar.setType(carDto.getType());
			existingCar.setYear(carDto.getYear());
			carRepository.save(existingCar);
			return true;
			
		}	
		else {
			return false;
		}
	}

	@Override
	public boolean BookACar(BookCarDto bookCarDto) {
		// TODO Auto-generated method stub
		Optional<Car> optionalCar=carRepository.findById(bookCarDto.getCarId());
		Optional<User> optionalUser=userRepository.findById(bookCarDto.getUserId());
		
		if(optionalCar.isPresent() && optionalUser.isPresent())
		{
			Car existingCar=optionalCar.get();
			BookACar bookACar=new BookACar();
			bookACar.setUser(optionalUser.get());
			bookACar.setCar(existingCar);
			bookACar.setBookCarStatus(BookCarStatus.PENDING);
			
			long diffInMilliSeconds=bookCarDto.getToDate().getTime() - bookCarDto.getFromDate().getTime();
			long days= TimeUnit.MICROSECONDS.toDays(diffInMilliSeconds);
			bookACar.setDays(days);
			bookACar.setPrice(existingCar.getPrice()*days);
			
			bookACarRepository.save(bookACar);
			return true;			
		}
		return false;
	}

	@Override
	public List<BookCarDto> getBookingByUserId(Long userId) {
		// TODO Auto-generated method stub
		return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
	}

	@Override
	public List<BookCarDto> getBooking() {
		// TODO Auto-generated method stub
		return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
	}

	@Override
	public boolean changeBookingStatus(Long bookingId, String status) {
		// TODO Auto-generated method stub
		Optional<BookACar> optionalBookACar=bookACarRepository.findById(bookingId);
		if(optionalBookACar.isPresent())
		{
			BookACar existingCar=optionalBookACar.get();
			if(Objects.equals(status,"APPROVED"))
			{
				existingCar.setBookCarStatus(BookCarStatus.APPROVED);
			}
			else {
				existingCar.setBookCarStatus(BookCarStatus.REJECTED);
			}
			bookACarRepository.save(existingCar);
			return true;
		}
		return false;
	}

	@Override
	public CarDtoListDto searchCars(SearchCarDto searchCarDto) {
		// TODO Auto-generated method stub
		Car car=new Car();
		car.setBrand(searchCarDto.getBrand());
		car.setTransmission(searchCarDto.getTransmission());
		car.setColor(searchCarDto.getColor());
		car.setType(searchCarDto.getType());
		
		ExampleMatcher exampleMatcher= ExampleMatcher.matchingAll()
											.withMatcher("brand",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
											.withMatcher("type",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
											.withMatcher("transmission",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
											.withMatcher("color",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Car> carExample=Example.of(car, exampleMatcher);
		List<Car> carList=carRepository.findAll(carExample);
		
		CarDtoListDto carDtoListDto=new CarDtoListDto();
		carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));											
		return carDtoListDto;
	}
}
