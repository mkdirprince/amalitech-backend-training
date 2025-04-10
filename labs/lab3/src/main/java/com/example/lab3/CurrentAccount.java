package com.example.lab3;

public class CurrentAccount extends BankAccount {
    private static final double OVERDRAFT_LIMIT = 100.00;

    //constructor with fixed overdraft
    public CurrentAccount(String accountName, double initialBalance, String type) {
        super(accountName, initialBalance, type);
    }

    // getter for overdraft limit
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        updateBalance(amount);
        addTransaction(amount, "CREDIT");
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        if (getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new IllegalStateException("Insufficient funds. Balance exceeded overdraft limit");
        }

        updateBalance(-amount);
        addTransaction(amount, "DEBIT");
    }
}
