package com.example.springboot.model;

import java.util.Date;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "booking")
public class Booking implements Archivable, Trackable {

    public Booking() {
        super();

        this.setCreatedAt(new Date());

        this.setUpdatedAt();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "court_id")
    protected Court court;

    @ManyToOne
    @JoinColumn(name = "member_id")
    protected Member member;

    protected String date;

    protected String startTime;

    protected String endTime;

    protected boolean cancelled;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date archivedAt;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    public Long getId() {
        return this.id;
    }

    public Long setId(Long id) {
        return this.id = id;
    }

    public Court getCourt() {
        return this.court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getArchivedAt() {
        return this.archivedAt;
    }

    public void setArchivedAt(Date archivedAt) {
        this.archivedAt = archivedAt;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCancelled() {
        return this.cancelled ? "Yes" : "No";
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
