package com.example.springboot.service;

import com.example.springboot.repository.TrainerRepository;
import java.util.List;
import java.util.ArrayList;
import com.example.springboot.model.Trainer;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    private TrainerRepository trainerRepository;

    // Dependency Injection
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<Trainer> getArchivedTrainers() {
        List<Trainer> archivedTrainers = new ArrayList<Trainer>();

        Iterable<Trainer> trainers = this.trainerRepository.findAll();

        for (Trainer trainer : trainers) {
            if (null != trainer.getArchivedAt()) {
                archivedTrainers.add(trainer);
            }
        }

        return archivedTrainers;
    }

    public List<Trainer> getActiveTrainers() {
        List<Trainer> activeTrainers = new ArrayList<Trainer>();

        Iterable<Trainer> trainers = this.trainerRepository.findAll();

        for (Trainer trainer : trainers) {
            if (null == trainer.getArchivedAt()) {
                activeTrainers.add(trainer);
            }
        }

        return activeTrainers;
    }
}
