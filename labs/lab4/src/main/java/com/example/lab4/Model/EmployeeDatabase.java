package com.example.lab4.Model;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    private final Map<T, Employee<T>> employees;
    private int size;


    public EmployeeDatabase() {
        this.employees = new HashMap<T, Employee<T>>();
        size = 0;
    }

    public Employee<T> addEmployee(Employee<T> employee){
        employees.put(employee.getEmployeeId(), employee);
        size += 1;
        return employee;
    }

    public void removeEmployee(T employeeId) {
        if (!employees.containsKey(employeeId)) {
            throw new IllegalStateException("Employee with ID " + employeeId + " not found.");

        }
        employees.remove(employeeId);
        size -= 1;
    }

    public Employee<T> updateEmployeeDetails(T employeeId, String field, Object newValue) {
        if (!employees.containsKey(employeeId)) {
            throw new IllegalStateException("Employee with ID " + employeeId + " not found.");
        }

        Employee<T> employeeToUpdate = employees.get(employeeId);

        switch (field) {
            case "name" -> employeeToUpdate.setName((String) newValue);
            case "department" -> employeeToUpdate.setDepartment((String) newValue);
            case "salary" -> employeeToUpdate.setSalary((double) newValue);
            case "performanceRating" -> employeeToUpdate.setPerformanceRating((double) newValue);
            case "yearsOfExperience" -> employeeToUpdate.setYearsOfExperience((int) newValue);
            case "isActive" -> employeeToUpdate.setIsActive((boolean) newValue);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        }

        return employeeToUpdate;
    }

    // returns a list of Employees which have the parameter department
    public List<Employee<T>> findByDepartment(String department){
        return employees.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    // returns  a list of Employees with same name as the parameter "name"
    public List<Employee<T>> findByName(String name) {
        return employees.values().stream()
                .filter(employee -> employee.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    // filters the  Employees with the minimum performance as the parameter
    public List<Employee<T>> findByMinPerformanceRating(double rating) {
        return employees.values().stream()
                .filter(employee -> employee.getPerformanceRating() >= rating)
                .collect(Collectors.toList());
    }

    // filters Employees based on salary range
    public List<Employee<T>> findBySalaryRange(double min, double max) {
        return employees.values().stream()
                .filter(employee -> employee.getSalary() >= min && employee.getSalary() <= max)
                .collect(Collectors.toList());
    }

    // returns a list of all employees
    public List<Employee<T>> getAllEmployees() {
        return List.copyOf(employees.values());
    }

    public int getSize() {
        return size;
    }

}
