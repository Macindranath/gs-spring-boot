package com.example.springboot.repository;

import com.example.springboot.model.Court;
import org.springframework.data.repository.CrudRepository;

// CourtRepository interface for managing Court entities.
public interface CourtRepository extends CrudRepository<Court, Long> {

}
