package com.example.springboot.factory;

import com.example.springboot.model.Member;
import com.example.springboot.model.User;

// Concrete Factory for Member objects
public class MemberFactory extends UserFactory {

    public User getInstance() {
        return new Member();
    }   
    
}
