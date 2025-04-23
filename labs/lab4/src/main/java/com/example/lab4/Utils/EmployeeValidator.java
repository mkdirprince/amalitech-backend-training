package com.example.lab4.Utils;

public class EmployeeValidator {

    public static void validateEmployeeInput(String name, String department, String salaryStr, String ratingStr, String experienceStr) {
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (!name.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Invalid name format");
        }

        // Validate department
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (!department.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Invalid department format");
        }

        // Validate salary
        try {
            double salary = Double.parseDouble(salaryStr);
            if (salary <= 0) {
                throw new IllegalArgumentException("Salary must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary format");
        }

        // Validate performance rating
        try {
            double rating = Double.parseDouble(ratingStr);
            if (rating < 0 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating format");
        }

        // Validate years of experience
        try {
            int experience = Integer.parseInt(experienceStr);
            if (experience < 0) {
                throw new IllegalArgumentException("Experience cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid experience format");
        }
    }

    public static void validateRating(String ratingStr) {
        if (ratingStr == null || ratingStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Rating cannot be empty");
        }
        try {
            double rating = Double.parseDouble(ratingStr);
            if (rating < 0) {
                throw new IllegalArgumentException("Rating must be non-negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating format");
        }
    }

    public static void validateSalaryRange(String minSalaryStr, String maxSalaryStr) {
        if (minSalaryStr == null || minSalaryStr.trim().isEmpty() || maxSalaryStr == null || maxSalaryStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Both salary values must be provided");
        }
        try {
            double minSalary = Double.parseDouble(minSalaryStr);
            double maxSalary = Double.parseDouble(maxSalaryStr);
            if (minSalary < 0) {
                throw new IllegalArgumentException("Minimum salary must be non-negative");
            }
            if (maxSalary < minSalary) {
                throw new IllegalArgumentException("Maximum salary must be greater than or equal to minimum salary");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary format");
        }
    }

    public static void validatePerformanceRaise(String ratingStr, String percentageStr) {
        if (ratingStr == null || ratingStr.trim().isEmpty() || percentageStr == null || percentageStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Both rating and percentage must be provided");
        }
        try {
            double rating = Double.parseDouble(ratingStr);
            double percentage = Double.parseDouble(percentageStr);
            if (rating < 0) {
                throw new IllegalArgumentException("Rating must be non-negative");
            }
            if (percentage < 0) {
                throw new IllegalArgumentException("Percentage must be non-negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating or percentage format");
        }
    }

    public static void validateTopN(String nStr) {
        if (nStr == null || nStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Number of employees must be provided");
        }
        try {
            int n = Integer.parseInt(nStr);
            if (n <= 0) {
                throw new IllegalArgumentException("Number of employees must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        }
    }

    public static void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a valid department");
        }
    }
}