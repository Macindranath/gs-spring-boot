package com.example.springboot.service;

import com.example.springboot.repository.TrainerRepository;
import java.util.List;
import com.example.springboot.model.Trainer;
import org.springframework.stereotype.Service;

// TrainerService class for managing Trainer entities.
@Service
public class TrainerService {

    
    private TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    /**
     * Gets all archived trainers.
     * This now calls the repository method.
     * The database does all the work.
     */
    public List<Trainer> getArchivedTrainers() {

        return this.trainerRepository.findByArchivedAtIsNotNull();
    }

    /**
     * Gets all active (non-archived) trainers.
     * This also calls the repository method.
     */
    public List<Trainer> getActiveTrainers() {

        return this.trainerRepository.findByArchivedAtIsNull();
    }




}
