package com.example.springboot.service;

import com.example.springboot.repository.ManagerRepository;
import java.util.List;
import com.example.springboot.model.Manager;
import org.springframework.stereotype.Service;


// ManagerService class for managing Manager entities.
@Service
public class ManagerService {

    private ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * Gets all archived managers.
     * This now calls the repository method.
     * The database does all the work.
     */
    public List<Manager> getArchivedManagers() {

        return this.managerRepository.findByArchivedAtIsNotNull();
    }

    /**
     * Gets all active (non-archived) managers.
     * This also calls the repository method.
     */
    public List<Manager> getActiveManagers() {

        return this.managerRepository.findByArchivedAtIsNull();
    }
}
