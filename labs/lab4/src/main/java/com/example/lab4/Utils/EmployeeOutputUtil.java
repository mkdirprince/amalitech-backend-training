package com.example.lab4.Utils;

import com.example.lab4.Model.Employee;
import com.example.lab4.Utils.EmployeeReportsUtil;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeOutputUtil<T> {
    private final EmployeeReportsUtil<T> reports = new EmployeeReportsUtil<>();

    // Prints the header for the employee details table
    public void printDetailsHeader() {
        System.out.println("-".repeat(109));
        System.out.printf("| %-10s | %-20s | %-10s | %-10s | %-12s | %-15s | %-10s |%n",
                "ID", "Name", "Department", "Salary", "Rating", "Experience", "Active");
        System.out.println("-".repeat(109));
    }

    // Prints the footer for the employee details table
    public void printDetailsFooter() {
        System.out.println("-".repeat(109));
    }

    // Prints a single employee's details in a formatted table row
    public void printEmployeeDetails(Employee<T> employee) {
        if (employee == null) {
            System.out.println("| No available employee data" + " ".repeat(93) + "|");
            return;
        }

        System.out.printf("| %-10s | %-20s | %-10s | $%-9.2f | %-12.1f | %-15d | %-10b |%n",
                employee.getEmployeeId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getPerformanceRating(),
                employee.getYearsOfExperience(),
                employee.getIsActive());
    }

    // Prints the header for reports based on departments
    public void printReportHeader() {
        System.out.println("-".repeat(86));
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s |%n",
                "Department", "Employee Count", "Avg Salary", "Avg Experience", "Avg Rating");
        System.out.println("-".repeat(86));
    }

    // footer for report
    public void printReportFooter() {
        System.out.println("-".repeat(86));
    }

    // Formats a single department report row as a string
    public String formatDepartmentReportRow(String department, long count, double avgSalary, double avgExperience, double avgRating) {
        return String.format("| %-10s | %-15d | $%-14.2f | %-15.1f | %-15.1f |",
                department != null ? department : "Unknown", count, avgSalary, avgExperience, avgRating);
    }


    // Prints a message for Empty employee details
    public void printEmptyEmployeesDetail() {
        printDetailsHeader();
        System.out.println("| No employees to display" + " ".repeat(83) + "|");
        printDetailsFooter();
    }

    // Prints a message for an empty department report
    public void printEmptyReport() {
        printReportHeader();
        System.out.println("| No department report to display" + " ".repeat(52) + "|");
        printReportFooter();
    }


    // prints the department reports
    public void printEmployeeReports(List<Employee<T>> employees) {
        reports.getEmployeeCountByDept(employees).entrySet().stream()
                .map(entry -> {
                    String dept = entry.getKey();
                    long count = entry.getValue();
                    double avgSalary = reports.getAvgSalaryByDept(employees).getOrDefault(dept, 0.0);
                    double avgExperience = reports.getAvgExperienceByDept(employees).getOrDefault(dept, 0.0);
                    double avgRating = reports.getAvgRatingByDept(employees).getOrDefault(dept, 0.0);
                    return formatDepartmentReportRow(dept, count, avgSalary, avgExperience, avgRating);
                })
                .forEach(System.out::println);

    }


}
