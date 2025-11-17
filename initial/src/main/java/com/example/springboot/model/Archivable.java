package com.example.springboot.model;

import java.util.Date;

public interface Archivable {

    public Date getArchivedAt();

    public void setArchivedAt(Date archivedAt);

}
