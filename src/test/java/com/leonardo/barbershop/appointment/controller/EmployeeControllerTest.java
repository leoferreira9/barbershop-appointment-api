package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEmployee() throws Exception{
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "(11) 90000-0000", "emp@email.com");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", true);

        when(employeeService.create(employeeRequest))
                .thenReturn(employeeResponse);

        mvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.email").value("emp@email.com"))
                .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.active").value(true));

        verify(employeeService).create(employeeRequest);
    }
}