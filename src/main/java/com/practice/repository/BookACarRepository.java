package com.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practice.entity.BookACar;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar, Long>{

	List<BookACar> findAllByUserId(Long userId);
}
