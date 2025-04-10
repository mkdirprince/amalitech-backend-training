package com.example.lab3;

public class SavingsAccount extends BankAccount {
    private static final double MINIMUM_BALANCE = 50.00;
    private static final double MINIMUM_DEPOSIT_AMOUNT = 5.00;
    private static final double ANNUAL_INTEREST_RATE = 0.015; // 1.5% annual rate
    private static  final int PERIODS_PER_YEAR = 12; //Monthly interest as default



    public SavingsAccount(String accountHolder, double initialBalance, String type) {
        super(accountHolder, initialBalance, type);
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Minimum amount for savings account is, " + MINIMUM_BALANCE);
        }
    }

    public void calculateInterest() {
        double periodicInterestRate = ANNUAL_INTEREST_RATE / PERIODS_PER_YEAR;
        double interest = balance * periodicInterestRate; // Monthly interest
        updateBalance(interest);
    }

    @Override
    public void deposit(double amount) {
        if (amount < MINIMUM_DEPOSIT_AMOUNT) {
            throw new IllegalArgumentException("Minimum deposit amount is 5.0");
        }
        updateBalance(amount);
        addTransaction(amount, "DEBIT");
    }

    @Override
    public void withdraw(double amount) {
        if (balance - amount < MINIMUM_BALANCE) {
            throw new IllegalStateException("Withdrawal denied: minimum balance of 50 required.");
        }

        calculateInterest();

        updateBalance(-amount);
        addTransaction(amount, "CREDIT");
    }

}
