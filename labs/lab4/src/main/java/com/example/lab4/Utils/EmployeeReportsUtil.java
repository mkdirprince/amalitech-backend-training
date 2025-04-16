package com.example.lab4.Utils;

import com.example.lab4.Model.Employee;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeReportsUtil<T> {


    // Group employees by department and calculate the counts
    public Map<String, Long> getEmployeeCountByDept(List<Employee<T>> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));
    }

    // Group average salary by departments
    public Map<String, Double> getAvgSalaryByDept(List<Employee<T>> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)
                ));
    }

    // Group average Experience by department
    public Map<String, Double> getAvgExperienceByDept(List<Employee<T>> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getYearsOfExperience)
                ));
    }

    // Group average Rating by department
    public Map<String, Double> getAvgRatingByDept(List<Employee<T>> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getPerformanceRating)
                ));
    }

}
