package com.example.springboot.model;

import java.util.Date;

// Interface for trackable entities
public interface Trackable {

    public Date getCreatedAt();

    public void setCreatedAt(Date createdAt);

    public Date getUpdatedAt();

    public void setUpdatedAt();

}
