package com.example.lab4.Model;

import java.util.UUID;

public class Employee<T> implements Comparable<Employee<T>> {
    //instance variables
    private final T employeeId;
    private String name; // full name
    private String department;
    private double salary;
    private double performanceRating; // 0 - 5 scale
    private int yearsOfExperience;
    private boolean isActive; // Employment status


    public Employee(T id, String name, String department, double pay, double rating, int years, boolean active) {
        this.employeeId = id;
        this.name = name;
        this.department = department;
        this.salary = pay;
        this.performanceRating = rating;
        this.yearsOfExperience = years;
        this.isActive = active;
    }

    // getters and setters
    public T getEmployeeId() {
        return this.employeeId;
    };

    public String getName() {
        return this.name;
    }

    public String getDepartment() {
        return this.department;
    }

    public double getSalary() {
        return this.salary;
    }

    public double getPerformanceRating(){
        return this.performanceRating;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    public void setPerformanceRating(double rating) {
        this.performanceRating = rating;
    }

    public void setYearsOfExperience(int years) {
        this.yearsOfExperience = years;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public int compareTo(Employee<T> other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare with null");
        }
        return Integer.compare(other.yearsOfExperience, this.yearsOfExperience);
    }

    @Override
    public String toString() {
        return "Employee {" +
                "ID=" + employeeId +
                ", Name='" + name + '\'' +
                ", Department='" + department + '\'' +
                ", Salary=" + salary +
                ", Performance=" + performanceRating +
                ", Experience=" + yearsOfExperience +
                ", Active=" + isActive +
                '}';
    }
}
