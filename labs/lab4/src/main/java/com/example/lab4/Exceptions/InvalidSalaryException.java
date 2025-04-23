package com.example.lab4.Exceptions;

public class InvalidSalaryException extends Exception {
    // default
    public InvalidSalaryException() {
        super("Salary cannot be negative");
    }

    public InvalidSalaryException(String message) {
        super(message);
    }
}
