package com.emp.service;

import com.emp.entity.Employee;
import com.emp.exceptions.EmployeeNotFoundException;
import com.emp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees() {
        //Given
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmailId("john.doe@example.com");
        employee1.setClub("Football");
        employee1.setSalary(50000.0);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmailId("jane.smith@example.com");
        employee2.setClub("Basketball");
        employee2.setSalary(60000.0);

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));

        //When
        List<Employee> employees = employeeService.getAllEmployees();

        //Then
        assertNotNull(employees);
        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findAll();

    }

    @Test
    void getEmployeeById_Found() throws EmployeeNotFoundException {
        // Given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmailId("john.doe@example.com");
        employee.setClub("Football");
        employee.setSalary(50000.0);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        //When
        Optional<Employee> result = Optional.ofNullable(employeeService.getEmployeeById(1L));

        //Then
        assertNotNull(result);
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        assertEquals("john.doe@example.com", result.get().getEmailId());
        assertEquals("Football", result.get().getClub());
        assertEquals(50000.0, result.get().getSalary());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(88L);
        });

        assertEquals("Employee with id 88 not found", ex.getMessage());
        verify(employeeRepository, times(1)).findById(88L);
    }
}