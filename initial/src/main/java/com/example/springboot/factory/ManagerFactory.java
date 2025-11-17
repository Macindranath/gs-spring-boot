package com.example.springboot.factory;

import com.example.springboot.factory.UserFactory;
import com.example.springboot.model.Manager;
import com.example.springboot.model.User;

public class ManagerFactory extends UserFactory {

    public User getInstance() {
        return new Manager();
    }
}
