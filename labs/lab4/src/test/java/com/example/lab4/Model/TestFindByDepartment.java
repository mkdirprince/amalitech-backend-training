package com.example.lab4.Model;

import com.example.lab4.Exceptions.EmployeeNotFoundException;
import com.example.lab4.Exceptions.InvalidDepartmentException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestFindByDepartment {

    private static EmployeeDatabase<UUID> database;
    private static UUID id1, id2, id3;

    @BeforeAll
    public static void setUp() {
        database = new EmployeeDatabase<>();

        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();
        id3 = UUID.randomUUID();

        Employee<UUID> emp1 = new Employee<>(id1, "Alice", "Engineering", 7000, 4.5, 5, true);
        Employee<UUID> emp2 = new Employee<>(id2, "Bob", "HR", 5000, 3.0, 3, true);
        Employee<UUID> emp3 = new Employee<>(id3, "Charlie", "engineering", 6500, 4.0, 4, true);

        database.addEmployee(emp1);
        database.addEmployee(emp2);
        database.addEmployee(emp3);
    }

    @Test
    public void testFindByDepartment () throws InvalidDepartmentException {
        List<Employee<UUID>> engineeringEmployees = database.findByDepartment("ENGINEERING");

        assertEquals(2, engineeringEmployees.size());
        assertTrue(engineeringEmployees.stream().anyMatch(e -> e.getEmployeeId().equals(id1)));
        assertTrue(engineeringEmployees.stream().anyMatch(e -> e.getEmployeeId().equals(id3)));
        assertNotNull(engineeringEmployees.getFirst());
        
        assertThrows(InvalidDepartmentException.class, () -> database.findByDepartment("MARKETING"));
    }
}
