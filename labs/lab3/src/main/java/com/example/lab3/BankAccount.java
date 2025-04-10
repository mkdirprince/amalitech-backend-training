package com.example.lab3;

import java.util.UUID;

public abstract class BankAccount implements BankOperations {
    private static int accountCounter = 1000000; // To start numbering accounts from this point

    protected String accountHolder;
    protected String accountType;
    protected String accountNumber;
    protected double balance;
    protected SLList transactionHistory;


    // constructor
    public BankAccount(String accountName, double initialBalance, String type) {
        this.accountHolder = accountName;
        this.accountType = type;
        this.accountNumber = generateAccountNumber();
        this.balance = initialBalance;
        this.transactionHistory = new SLList(new Transaction(initialBalance, "INITIAL"));
    }

    // Method to generate unique account number
    private String generateAccountNumber() {
        return String.format("010%d02", accountCounter++);
    }

    //getters

    // returns the name of the account holder
    public String getAccountHolder() {
        return accountHolder;
    }

    // returns the account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // returns the account type
    public String getAccountType() {
        return accountType;
    }

    //returns the account balance
    public double getBalance() {
        return balance;
    }


    public int getDuration() {
        return 0; // Default for non-fixed accounts
    };

    public void updateBalance(double amount) {
        this.balance += amount;
    }

    public void addTransaction(double amount, String type) {
        this.transactionHistory.addFirst(new Transaction(amount, type));
    }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

}
