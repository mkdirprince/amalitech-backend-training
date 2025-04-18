package com.example.lab4;

import com.example.lab4.Model.Employee;
import com.example.lab4.Model.EmployeeDatabase;

public class Main {
    public static void main(String[] args) {
        EmployeeDatabase<Integer> database = new EmployeeDatabase<>();
        database.addEmployee(new Employee<>(1, "John Doe", "IT", 75000.0, 4.5, 10, true));
        database.addEmployee(new Employee<>(2, "Jane Smith", "HR", 65000.0, 4.0, 5, true));
        database.addEmployee(new Employee<>(3, "Bob Johnson", "Finance", 80000.0, 3.8, 8, false));
        database.addEmployee(new Employee<>(4, "Alice Brown", "IT", 85000.0, 4.8, 12, true));

        System.out.println("Employee Details:");
        database.displayEmployeeDetails();

        System.out.println("\nDepartment Report:");
        database.displayEmployeeReports();

        // Test empty database
        EmployeeDatabase<Integer> emptyDatabase = new EmployeeDatabase<>();
        System.out.println("\nEmpty Database Details:");
        emptyDatabase.displayEmployeeDetails();

        System.out.println("\nEmpty Department Report:");
        emptyDatabase.displayEmployeeReports();
    }
}
