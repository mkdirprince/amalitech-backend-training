package com.example.lab4.Service;

import com.example.lab4.Exceptions.EmployeeNotFoundException;
import com.example.lab4.Exceptions.InvalidDepartmentException;
import com.example.lab4.Exceptions.InvalidSalaryException;
import com.example.lab4.Model.Employee;
import com.example.lab4.Model.EmployeeDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EmployeeService {
    private final ObservableList<Employee<UUID>> employees;
    private final EmployeeDatabase<UUID> employeeDatabase;

    public EmployeeService() {
        employees = FXCollections.observableArrayList();
        employeeDatabase = new EmployeeDatabase<>();
    }

    public void addEmployee(Employee<UUID> employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        employees.add(employee);
        employeeDatabase.addEmployee(employee);
    }

    public void removeEmployee(Employee<UUID> employee) throws EmployeeNotFoundException {
        employees.remove(employee);
        employeeDatabase.removeEmployee(employee.getEmployeeId());
    }

    public void updateEmployee(Employee<UUID> updatedEmployee) throws EmployeeNotFoundException, InvalidSalaryException {
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Updated employee cannot be null");
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(updatedEmployee.getEmployeeId())) {
                employees.set(i, updatedEmployee);
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "name", updatedEmployee.getName()
                );
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "department", updatedEmployee.getDepartment()
                );
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "salary", updatedEmployee.getSalary()
                );
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "performanceRating", updatedEmployee.getPerformanceRating()
                );
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "yearsOfExperience", updatedEmployee.getYearsOfExperience()
                );
                employeeDatabase.updateEmployeeDetails(
                        updatedEmployee.getEmployeeId(),
                        "isActive", updatedEmployee.getIsActive()
                );
                return;
            }
        }
        throw new EmployeeNotFoundException("Employee with ID " + updatedEmployee.getEmployeeId() + " not found");
    }

    public ObservableList<Employee<UUID>> getAllEmployees() {
        return employees;
    }

    public Set<String> getAllDepartments() {
        Set<String> departments = new HashSet<>();
        employees.forEach(employee -> departments.add(employee.getDepartment()));
        return departments;
    }


    public void applyPerformanceRaise(double rating, double percentageRaise) {
        employeeDatabase.applyPerformanceRaise(rating, percentageRaise);
        // Assume ObservableList is synced via database binding or shared reference
        // No need to manually update employees list
    }

    public List<Employee<UUID>> getTopNHighestPaidEmployees(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of employees must be positive");
        }
        // Convert List<Employee<UUID>> to ObservableList
        return employeeDatabase.getTopNHighestPaidEmployee(n);
    }

    public double getAverageSalaryByDepartment(String department) throws InvalidDepartmentException {
        return employeeDatabase.getAverageSalaryByDepartment(department);
    }
}