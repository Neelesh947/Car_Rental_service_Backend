package com.practice.dto;

import java.util.Date;

import com.practice.enums.BookCarStatus;

import lombok.Data;

@Data
public class BookCarDto {

	private Long id;
	
	private Date toDate;
	private Date fromDate;
	private Long days;
	private Long price;
	private BookCarStatus bookCarStatus;
	
	private Long userId;
	private Long carId;
	
	private String username;
	private String email;
}
