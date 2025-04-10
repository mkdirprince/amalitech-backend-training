package com.example.lab3;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FixedDepositAccount extends BankAccount{
    private static final double MINIMUM_PRINCIPAL = 50000.00;
    private static final int MAXIMUM_DURATION = 365;
    private static final int MINIMUM_DURATION = 90;
    private final int duration;
    private final LocalDate depositDate;
    private final double interestRate;

    public FixedDepositAccount(String accountName, int principal, String type, int period) {
        super(accountName, principal, type);

        if (principal < MINIMUM_PRINCIPAL) {
            throw new IllegalArgumentException("Minimum principal for the fixed deposit is 50,000");
        }

        if (period > MAXIMUM_DURATION) {
            throw new IllegalArgumentException("Maximum duration for the fixed deposit is 365 days");
        }
        if (period < MINIMUM_DURATION) {
            throw new IllegalArgumentException("Minimum duration for the fixed deposit is 90 days");
        }

        this.duration = period;
        this.depositDate = LocalDate.now();
        this.interestRate = getInterestRate();

    }


    public double getInterestRate() {
        if (duration < 180) {
            return 0.12;
        } else if (duration < 270) {
            return 0.15;
        } else {
            return 0.18;
        }
    }



    private boolean isMatured() {
        LocalDate currentDate = LocalDate.now();

        long daysActive = ChronoUnit.DAYS.between(currentDate, depositDate);

        return daysActive >= duration;
    }

    public void calculateInterest() {
        if (!isMatured()) {
            throw new IllegalStateException("The fixed deposit is still active. Interest cannot be calculated yet.");
        }

        double interestAmount = balance * interestRate * ((double) duration / MAXIMUM_DURATION);

        updateBalance(interestAmount);
        addTransaction(interestAmount, "DEBIT");
    }


    @Override
    public int getDuration() {
        return  this.duration;
    }


    @Override
    public void deposit(double amount) {
        throw new UnsupportedOperationException("Deposits not allowed for Fixed Deposit Account");
    }

    @Override
    public void withdraw(double amount) {
        if (!isMatured()) {  // Withdrawals are not allowed before maturity
            throw new IllegalStateException("The fixed deposit is not yet matured. Withdrawal is not allowed.");
        }

        calculateInterest(); // calculate interest before withdrawing

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds to withdraw the specified amount.");
        }

        updateBalance(-amount);
        addTransaction(amount, "CREDIT");
    }

}
