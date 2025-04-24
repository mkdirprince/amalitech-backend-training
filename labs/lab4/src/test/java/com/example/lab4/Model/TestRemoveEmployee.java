package com.example.lab4.Model;

import com.example.lab4.Exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestRemoveEmployee {
    private static final EmployeeDatabase<UUID> database = new EmployeeDatabase<>();
    private static final UUID employeeId = UUID.randomUUID();

    @BeforeEach
    public void setup(){
        Employee<UUID> employee1 = new Employee<>(employeeId, "Alice Dankwah", "HR", 5000, 3.0, 6, true);
        Employee<UUID> employee2 = new Employee<>(UUID.randomUUID() ,"John Doe", "IT", 75000.0, 4.5, 10, true);
        database.addEmployee(employee1);
        database.addEmployee(employee2);
    }

    @Test
    public void testRemoveEmployee() throws EmployeeNotFoundException {

        int initialSize = database.getSize();

       //remove employee
        database.removeEmployee(employeeId);

        int sizeAfter = database.getSize();

        assertEquals(initialSize - 1, sizeAfter);

        assertThrows(EmployeeNotFoundException.class, () -> database.removeEmployee(employeeId));

    }
}
