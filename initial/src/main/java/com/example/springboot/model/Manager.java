package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "manager")
public class Manager extends User {

    public Manager() {
        super();

        this.setCreatedAt(new Date());

        this.setUpdatedAt();
    }
}
