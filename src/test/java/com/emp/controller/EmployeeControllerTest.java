package com.emp.controller;

import com.emp.entity.Employee;
import com.emp.exceptions.EmployeeNotFoundException;
import com.emp.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void testGetAllEmployees() throws Exception {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        Mockito.when(employeeService.getEmployeeById(Mockito.anyLong())).thenThrow(new EmployeeNotFoundException("Employee with 88 Not Found"));
        mockMvc.perform(get("/api/employees/88")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee with 88 Not Found"))
                .andExpect(jsonPath("$.status").value("404")
                );
    }

    @Test
    void testGetEmployeeByIdFound() throws Exception {
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setFirstName("Pedro");
        mockEmployee.setLastName("Neto");
        mockEmployee.setEmailId("pn@chelsea.com");
        mockEmployee.setClub("Chelsea");
        mockEmployee.setSalary(100000.0);

        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(mockEmployee);
        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Pedro"))
                .andExpect(jsonPath("$.lastName").value("Neto"))
                .andExpect(jsonPath("$.emailId").value("pn@chelsea.com"))
                .andExpect(jsonPath("$.club").value("Chelsea"))
                .andExpect(jsonPath("$.salary").value("100000.0"));
    }
}