package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.MappedSuperclass;

public interface Trackable {

    public Date getCreatedAt();

    public void setCreatedAt(Date createdAt);

    public Date getUpdatedAt();

    public void setUpdatedAt();

}
