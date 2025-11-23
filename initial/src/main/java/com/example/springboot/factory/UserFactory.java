package com.example.springboot.factory;

import com.example.springboot.model.User;

// Abstract Factory for User objects
public abstract class UserFactory {

    public abstract User getInstance();
}
