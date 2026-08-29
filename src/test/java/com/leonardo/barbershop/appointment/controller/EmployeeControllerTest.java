package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.employee.EmployeePatchRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void shouldReturnConflictWhenCreatingEmployeeWithRegisteredEmail() throws Exception{
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "(11) 90000-0000", "emp@email.com");

        when(employeeService.create(employeeRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + employeeRequest.email() + " already registered!"));

        mvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest))
        ).andExpect(status().isConflict());

        verify(employeeService).create(employeeRequest);
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidEmployee() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "(1234) 90000-0000", "emp@email.com");

        mvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest))
        ).andExpect(status().isBadRequest());

        verify(employeeService, never()).create(any());
    }

    @Test
    void shouldFindEmployeeById() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", true);

        when(employeeService.findById(id))
                .thenReturn(employeeResponse);

        mvc.perform(
                get("/api/v1/employees/{id}", id)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.email").value("emp@email.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(employeeService).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(employeeService.findById(id))
                .thenThrow(new EntityNotFoundException("Employee not found with ID: " + id));

        mvc.perform(
                get("/api/v1/employees/{id}", id)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + id));

        verify(employeeService).findById(id);
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", true);
        List<EmployeeResponse> list = List.of(employeeResponse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<EmployeeResponse> page = new PageImpl<>(list, pageable, 1);

        when(employeeService.findAll(null, null, pageable))
                .thenReturn(page);

        mvc.perform(
                get("/api/v1/employees")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(id.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Name"))
                .andExpect(jsonPath("$.content[0].phone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.content[0].email").value("emp@email.com"))
                .andExpect(jsonPath("$.content[0].active").value(true));

        verify(employeeService).findAll(null, null, pageable);
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest("Name", "(11) 90000-0000", "emp@email.com");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", true);

        when(employeeService.update(id, updateRequest))
                .thenReturn(employeeResponse);

        mvc.perform(
                put("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.email").value("emp@email.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(employeeService).update(id, updateRequest);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest("Name", "(11) 90000-0000", "emp@email.com");

        when(employeeService.update(id, updateRequest))
                .thenThrow(new EntityNotFoundException("Employee not found with ID: " + id));

        mvc.perform(
                put("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + id));

        verify(employeeService).update(id, updateRequest);
    }

    @Test
    void shouldReturnConflictWhenUpdatingEmployeeWithRegisteredEmail() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest("Name", "(11) 90000-0000", "emp@email.com");

        when(employeeService.update(id, updateRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + updateRequest.email() + " already registered!"));

        mvc.perform(
                put("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email " + updateRequest.email() + " already registered!"));

        verify(employeeService).update(id, updateRequest);
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingInvalidEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeUpdateRequest updateRequest =
                new EmployeeUpdateRequest("", "(11) 90000-0000", "emp@email.com");

        mvc.perform(
                put("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpect(status().isBadRequest());

        verify(employeeService, never())
                .update(any(UUID.class), any(EmployeeUpdateRequest.class));
    }

    @Test
    void shouldPartiallyUpdateEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeePatchRequest patchRequest = new EmployeePatchRequest(null, null, "emp2@gmail.com");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp2@gmail.com", true);

        when(employeeService.partialUpdate(id, patchRequest))
                .thenReturn(employeeResponse);

        mvc.perform(
                patch("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id.toString()))
                        .andExpect(jsonPath("$.name").value("Name"))
                        .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                        .andExpect(jsonPath("$.email").value("emp2@gmail.com"))
                        .andExpect(jsonPath("$.active").value(true));

        verify(employeeService).partialUpdate(id, patchRequest);
    }

    @Test
    void shouldReturnNotFoundWhenPartiallyUpdatingNonexistentEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeePatchRequest patchRequest = new EmployeePatchRequest(null, null, "emp2@gmail.com");

        when(employeeService.partialUpdate(id, patchRequest))
                .thenThrow(new EntityNotFoundException("Employee not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + id));

        verify(employeeService).partialUpdate(id, patchRequest);
    }

    @Test
    void shouldReturnConflictWhenPartiallyUpdatingEmployeeWithRegisteredEmail() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeePatchRequest patchRequest = new EmployeePatchRequest(null, null, "emp2@gmail.com");

        when(employeeService.partialUpdate(id, patchRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + patchRequest.email() + " already registered!"));

        mvc.perform(
                patch("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email " + patchRequest.email() + " already registered!"));

        verify(employeeService).partialUpdate(id, patchRequest);
    }

    @Test
    void shouldReturnBadRequestWhenPartiallyUpdatingEmployeeWithNameExceedingMaxLength() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        EmployeePatchRequest patchRequest = new EmployeePatchRequest(
                "a".repeat(151),
                "(11) 90000-0000",
                "emp2@gmail.com"
        );

        mvc.perform(
                patch("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }

    @Test
    void shouldDeactivateEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", false);

        when(employeeService.deactivate(id))
                .thenReturn(employeeResponse);

        mvc.perform(
                patch("/api/v1/employees/{id}/deactivate", id)
        ).andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id.toString()))
                        .andExpect(jsonPath("$.name").value("Name"))
                        .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                        .andExpect(jsonPath("$.email").value("emp@email.com"))
                        .andExpect(jsonPath("$.active").value(false));

        verify(employeeService).deactivate(id);
    }

    @Test
    void shouldReturnNotFoundWhenDeactivatingNonexistentEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(employeeService.deactivate(id))
                .thenThrow(new EntityNotFoundException("Employee not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/employees/{id}/deactivate", id)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + id));

        verify(employeeService).deactivate(id);
    }

    @Test
    void shouldReturnConflictWhenDeactivatingAlreadyDeactivatedEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(employeeService.deactivate(id))
                .thenThrow(new EntityAlreadyDeactivatedException("Employee already deactivated"));

        mvc.perform(
                patch("/api/v1/employees/{id}/deactivate", id)
        ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Employee already deactivated"));

        verify(employeeService).deactivate(id);
    }

    @Test
    void shouldActivateEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        EmployeeResponse employeeResponse = new EmployeeResponse(id, "Name", "(11) 90000-0000", "emp@email.com", true);

        when(employeeService.activate(id))
                .thenReturn(employeeResponse);

        mvc.perform(
                patch("/api/v1/employees/{id}/activate", id)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.phone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.email").value("emp@email.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(employeeService).activate(id);
    }

    @Test
    void shouldReturnNotFoundWhenActivatingNonexistentEmployee() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(employeeService.activate(id))
                .thenThrow(new EntityNotFoundException("Employee not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/employees/{id}/activate", id)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with ID: " + id));

        verify(employeeService).activate(id);
    }
}
