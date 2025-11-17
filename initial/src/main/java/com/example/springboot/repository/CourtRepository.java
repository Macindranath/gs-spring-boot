package com.example.springboot.repository;

import com.example.springboot.model.Court;
import org.springframework.data.repository.CrudRepository;

public interface CourtRepository extends CrudRepository<Court, Long> {

}
