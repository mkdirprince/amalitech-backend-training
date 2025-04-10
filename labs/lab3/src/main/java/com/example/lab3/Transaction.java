package com.example.lab3;

import java.time.LocalDate;

public class Transaction {
    private final double amount;
    private final String type;
    private final LocalDate timestamp;

    public Transaction(double amount, String type) {
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDate.now();
    }


    //getter
    public double getAmount() {
        return this.amount;
    }


    public String getType(){
        return this.type;
    }

    public LocalDate getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f at %s", type, amount, timestamp);
    }
}
