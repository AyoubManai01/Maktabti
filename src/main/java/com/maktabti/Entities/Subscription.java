package com.maktabti.Entities;

import java.time.LocalDate;

public class Subscription {
    private int id;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private double fine;

    public Subscription(int id, String email, LocalDate startDate, LocalDate endDate, double fine) {
        this.id = id;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fine = fine;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
