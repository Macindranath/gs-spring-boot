package com.example.springboot.model;

import jakarta.persistence.Entity;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.Id;

@Entity
class Operation implements GrantedAuthority {
    @Id
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}