package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

// Member entity representing a system member.
@Entity
@Table(name = "member")
public class Member extends User {

    public Member() {
        super();

        this.setCreatedAt(new Date());

        this.setUpdatedAt();
    }

}
