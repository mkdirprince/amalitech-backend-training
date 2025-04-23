package com.example.lab4.Exceptions;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message){
        super(message);
    }
}
