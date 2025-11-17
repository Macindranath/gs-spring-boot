package com.example.springboot.repository;

import com.example.springboot.model.Manager;
import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Long> {

}
