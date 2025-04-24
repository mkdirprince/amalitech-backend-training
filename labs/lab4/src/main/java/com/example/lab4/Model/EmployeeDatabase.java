package com.example.lab4.Model;

import com.example.lab4.Exceptions.EmployeeNotFoundException;
import com.example.lab4.Exceptions.InvalidDepartmentException;
import com.example.lab4.Exceptions.InvalidSalaryException;
import com.example.lab4.Utils.EmployeeSalaryComparator;
import com.example.lab4.Utils.EmployeeOutputUtil;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    private static final double PERCENT_DENOMINATOR = 100.0;
    private final HashMap<T, Employee<T>> employees;
    private final EmployeeOutputUtil<T> printer = new EmployeeOutputUtil<>();
    private int size;


    public EmployeeDatabase() {
        this.employees = new HashMap<T, Employee<T>>();
        size = 0;
    }

    public void addEmployee(Employee<T> employee){

        if (employee == null) {
            throw new IllegalArgumentException("Cannot add null employee.");
        }

        employees.put(employee.getEmployeeId(), employee);
        size += 1;
    }

    public void removeEmployee(T employeeId) throws EmployeeNotFoundException {
        if (!employees.containsKey(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");

        }
        employees.remove(employeeId);
        size -= 1;
    }

    // helper function to update employee details
    public void updateEmployeeField( Employee<T> employeeToUpdate, String field, Object value) throws InvalidSalaryException {
        switch (field) {
            case "name" -> employeeToUpdate.setName((String) value);
            case "department" -> employeeToUpdate.setDepartment((String) value);
            case "salary" -> {
                double newSalary = (double) value;
                if (newSalary < 0) {
                    throw new InvalidSalaryException("Cannot update salary to a negative value: " + newSalary);
                }
                employeeToUpdate.setSalary(newSalary);
            }
            case "performanceRating" -> employeeToUpdate.setPerformanceRating((double) value);
            case "yearsOfExperience" -> employeeToUpdate.setYearsOfExperience((int) value);
            case "isActive" -> employeeToUpdate.setIsActive((boolean) value);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    public Employee<T> updateEmployeeDetails(T employeeId, String field, Object newValue) throws EmployeeNotFoundException, InvalidSalaryException {
        if (!employees.containsKey(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }

        // bug for debugging
        Employee<T> employeeToUpdate = employees.values().stream().findFirst().orElse(null);

        updateEmployeeField(employeeToUpdate, field, newValue);

        return employeeToUpdate;
    }

    // returns a list of Employees which have the parameter department
    public List<Employee<T>> findByDepartment(String department) throws InvalidDepartmentException{
        List<Employee<T>> result = employees.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new InvalidDepartmentException("No employees found in department: " + department);
        }

        return result;
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

    // give raise based on performance rating
    public void applyPerformanceRaise(double rating, double percentageRaise){
        employees.values().stream()
                .filter(employee -> employee.getPerformanceRating() >= rating)
                .forEach(employee -> {
                    try {
                        employee.setSalary(
                                Math.round(employee.getSalary() * (1 + (percentageRaise / PERCENT_DENOMINATOR))
                                ));
                    } catch (InvalidSalaryException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    public List<Employee<T>> getTopNHighestPaidEmployee(int n) {
        return employees.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .limit(n)
                .collect(Collectors.toList());
    }

    public double getAverageSalaryByDepartment(String department) throws InvalidDepartmentException {

        if (department == null || department.trim().isEmpty()) {
            throw new InvalidDepartmentException("Department cannot be null or empty");
        }

        boolean departmentExists = employees.values().stream()
                .anyMatch(employee -> employee.getDepartment().equalsIgnoreCase(department));

        if (!departmentExists) {
            throw new InvalidDepartmentException("Unknown departement: " + department);
        }

        return employees.values().stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow(() -> new InvalidDepartmentException("No employee found in the department: " + department));
    }


    // returns a list of all employees
    public List<Employee<T>> getAllEmployees() {
        return List.copyOf(employees.values());
    }

    public Iterator<Employee<T>> getEmployeeIterator(){
        return employees.values().iterator();
    }

    public int getSize() {
        return size;
    }

    public void displayEmployeeDetails() {
        if (employees.isEmpty()) {
            printer.printEmptyEmployeesDetail();
            return;
        }

        // Header
        printer.printDetailsHeader();

        // Each employee's details
        for (Employee<T> employee : employees.values()) {
            printer.printEmployeeDetails(employee);
        }

        // Footer
        printer.printDetailsFooter();
    }

    public void displayEmployeeReports() {
        if (employees.isEmpty()) {
            printer.printEmptyReport();
            return;
        }

        // Header
        printer.printReportHeader();

        // Reports
        printer.printEmployeeReports(employees.values().stream().toList());

        // Footer
        printer.printReportFooter();
    }

}
