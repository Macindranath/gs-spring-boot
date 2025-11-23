package com.example.springboot.repository;

import com.example.springboot.model.Manager;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ManagerRepository extends CrudRepository<Manager, Long> {


    /**
     * This is a custom query method. Spring Data JPA will
     * automatically generate the SQL query:
     * "SELECT * FROM manager WHERE archived_at IS NULL" for speed and efficiency.
     */
    List<Manager> findByArchivedAtIsNull();

    /**
     * This one will automatically generate:
     * "SELECT * FROM manager WHERE archived_at IS NOT NULL"
     */
    List<Manager> findByArchivedAtIsNotNull();
}