package com.example.lab4.Model;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestAddEmployee {

    private final EmployeeDatabase<UUID> database = new EmployeeDatabase<>();;


    @Test
    void testAddEmployee() {
        UUID addedKey = UUID.randomUUID();
        Employee<UUID> validEmployee = new Employee<>(addedKey, "Alice Dankwah", "HR", 5000, 3.0, 6, true);

        // Add valid employee
        database.addEmployee(validEmployee);
        assertEquals(1, database.getSize());

        // Should be able to get the retrieve the added employee
        Optional<Employee<UUID>> retrievedEmployee = database.getAllEmployees()
                .stream()
                .filter(employee -> employee.getEmployeeId().equals(addedKey))
                .findFirst();

        assertNotNull(retrievedEmployee);


        // Add null should throw exception
        assertThrows(IllegalArgumentException.class, () -> database.addEmployee(null));
    }

}