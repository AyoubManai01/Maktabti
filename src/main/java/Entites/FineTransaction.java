package Entites;

import java.util.Date;

public class FineTransaction {
    protected double amount;
    protected Date creationDate;

    public FineTransaction(double amount) {
        this.creationDate = new Date(); // Automatically set current date
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public boolean initiateTransaction() {
        System.out.println("Processing fine transaction of $" + amount);
        return amount > 0; // Basic validation
    }
}
