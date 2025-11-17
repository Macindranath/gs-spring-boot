package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@Table(name = "trainer")
public class Trainer extends User {

    @Column(columnDefinition = "TEXT")
    protected String description;

    public Trainer() {
        super();

        this.setCreatedAt(new Date());

        this.setUpdatedAt();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
