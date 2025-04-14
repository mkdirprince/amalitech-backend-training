package com.example.lab4.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public Collection<Employee<T>> getAllEmployees() {
        return employees.values();
    }

    public int getSize() {
        return size;
    }

}
