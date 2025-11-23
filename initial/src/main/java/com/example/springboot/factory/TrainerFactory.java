package com.example.springboot.factory;

import com.example.springboot.factory.UserFactory;
import com.example.springboot.model.Trainer;
import com.example.springboot.model.User;

// Concrete Factory for Trainer objects
public class TrainerFactory extends UserFactory {

    public User getInstance() {
        return new Trainer();
    }           
    
}
