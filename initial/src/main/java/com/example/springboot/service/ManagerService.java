package com.example.springboot.service;

import com.example.springboot.repository.ManagerRepository;
import java.util.List;
import java.util.ArrayList;
import com.example.springboot.model.Manager;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private ManagerRepository managerRepository;

    // Dependency Injection
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> getArchivedManagers() {
        List<Manager> archivedManagers = new ArrayList<Manager>();

        Iterable<Manager> managers = this.managerRepository.findAll();

        for (Manager manager : managers) {
            if (null != manager.getArchivedAt()) {
                archivedManagers.add(manager);
            }
        }

        return archivedManagers;
    }

    public List<Manager> getActiveManagers() {
        List<Manager> activeManagers = new ArrayList<Manager>();

        Iterable<Manager> managers = this.managerRepository.findAll();

        for (Manager manager : managers) {
            if (null == manager.getArchivedAt()) {
                activeManagers.add(manager);
            }
        }

        return activeManagers;
    }
}
