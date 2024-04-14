package com.practice.entity;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.dto.BookCarDto;
import com.practice.enums.BookCarStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BookACar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date toDate;
	private Date fromDate;
	private Long days;
	private Long price;
	private BookCarStatus bookCarStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="car_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Car car;
	
	public BookCarDto getBookACarDto()
	{
		BookCarDto bookCarDto=new BookCarDto();
		bookCarDto.setId(id);
		bookCarDto.setDays(days);
		bookCarDto.setBookCarStatus(bookCarStatus);
		bookCarDto.setPrice(price);
		bookCarDto.setToDate(toDate);
		bookCarDto.setFromDate(fromDate);
		bookCarDto.setEmail(user.getEmail());
		bookCarDto.setUsername(user.getUsername());
		bookCarDto.setUserId(user.getId());
		bookCarDto.setCarId(car.getId());
		
		return bookCarDto;
	}
}
