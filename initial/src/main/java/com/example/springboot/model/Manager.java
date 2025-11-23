package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

// Manager entity representing a system manager.
@Entity
@Table(name = "manager")
public class Manager extends User {

    public Manager() {
        super();

        this.setCreatedAt(new Date());

        this.setUpdatedAt();
    }
}
