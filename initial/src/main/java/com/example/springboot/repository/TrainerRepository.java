package com.example.springboot.repository;

import com.example.springboot.model.Trainer;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

// TrainerRepository interface for managing Trainer entities.
public interface TrainerRepository extends CrudRepository<Trainer, Long> {

        /**
     * This is a custom query method. Spring Data JPA will
     * automatically generate the SQL query:
     * "SELECT * FROM trainer WHERE archived_at IS NULL" for speed and efficiency.
     */
    List<Trainer> findByArchivedAtIsNull();

    /**
     * This one will automatically generate:
     * "SELECT * FROM trainer WHERE archived_at IS NOT NULL"
     */
    List<Trainer> findByArchivedAtIsNotNull();
}

