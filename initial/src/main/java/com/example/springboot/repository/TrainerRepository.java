package com.example.springboot.repository;

import com.example.springboot.model.Trainer;
import org.springframework.data.repository.CrudRepository;

public interface TrainerRepository extends CrudRepository<Trainer, Long> {

}
