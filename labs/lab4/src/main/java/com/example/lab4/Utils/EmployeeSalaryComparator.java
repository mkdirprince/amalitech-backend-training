package com.example.lab4.Utils;

import com.example.lab4.Model.Employee;

import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {
    public int compare(Employee<T> employeeOne, Employee<T> employeeTwo) {
        return  Double.compare(employeeTwo.getSalary(), employeeOne.getSalary());
    }
}
