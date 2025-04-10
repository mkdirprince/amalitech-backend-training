package com.example.lab3;

// Interface for common banking operations
public interface BankOperations {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}
