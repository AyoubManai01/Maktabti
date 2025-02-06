package com.maktabti.Entities;

import java.time.LocalDate;

public class Subscription {
    private int id;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double fine;

    public Subscription(int id, int userId, LocalDate startDate, LocalDate endDate, double fine) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fine = fine;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
