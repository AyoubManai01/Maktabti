package com.maktabti.Entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Subscription {
    private final StringProperty email;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;
    private final DoubleProperty fine;
    private final int id;

    // Constructor
    public Subscription(int id, String email, LocalDate startDate, LocalDate endDate, double fine) {
        this.id = id;
        this.email = new SimpleStringProperty(email);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.fine = new SimpleDoubleProperty(fine);
    }

    // Getter and setter methods for each property
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public double getFine() {
        return fine.get();
    }

    public void setFine(double fine) {
        this.fine.set(fine);
    }

    public DoubleProperty fineProperty() {
        return fine;
    }

    public int getId() {
        return id;
    }
}
