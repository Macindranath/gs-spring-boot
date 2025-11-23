package com.example.springboot.model;

import java.util.Date;

// Interface for archivable entities
public interface Archivable {

    public Date getArchivedAt();

    public void setArchivedAt(Date archivedAt);

}
