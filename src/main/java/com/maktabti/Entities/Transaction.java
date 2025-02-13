package com.maktabti.Entities;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int userId;
    private int bookId;
    private LocalDateTime transactionDate;
    private String type;

    public Transaction(int id, int userId, int bookId, LocalDateTime transactionDate, String type) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.transactionDate = transactionDate;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}