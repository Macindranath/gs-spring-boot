package com.example.springboot.factory;

import com.example.springboot.factory.UserFactory;
import com.example.springboot.model.Member;
import com.example.springboot.model.User;

public class MemberFactory extends UserFactory {

    public User getInstance() {
        return new Member();
    }   
    
}
