package com.example.springboot.factory;

import com.example.springboot.model.Manager;
import com.example.springboot.model.User;

// Concrete Factory for Manager objects
public class ManagerFactory extends UserFactory {

    public User getInstance() {
        return new Manager();
    }
}
