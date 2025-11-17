package com.example.springboot.repository;

import com.example.springboot.model.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

}
