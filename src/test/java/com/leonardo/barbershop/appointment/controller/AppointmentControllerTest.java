package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentRequest;
import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.exception.EmployeeNotAvailable;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.exception.ServiceItemNotAvailable;
import com.leonardo.barbershop.appointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @MockitoBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateAppointmentWithoutErrors() throws Exception {
        UUID clientId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID employeeId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID serviceItemId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        String expectedAppointmentDate = futureDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                clientId,
                employeeId,
                serviceItemId,
                futureDate
        );

        AppointmentResponse appointmentResponse = new AppointmentResponse(
               UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a"),
                "Firstname",
                "Lastname",
                "(11) 90000-0000",
                "client@email.com",
                "EmployeeName",
                "(11) 80000-0000",
                "emp@email.com",
                "ServiceItemName",
                "serviceItemDescription",
                new BigDecimal("40"),
                10,
                futureDate,
                AppointmentStatus.SCHEDULED
        );

        when(appointmentService.create(appointmentRequest))
                .thenReturn(appointmentResponse);


        mvc.perform(
                post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("810b60ad-e152-4656-a8f5-eb8c4d35633a"))
                .andExpect(jsonPath("$.clientFirstName").value("Firstname"))
                .andExpect(jsonPath("$.clientLastName").value("Lastname"))
                .andExpect(jsonPath("$.clientPhone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.clientEmail").value("client@email.com"))
                .andExpect(jsonPath("$.employeeName").value("EmployeeName"))
                .andExpect(jsonPath("$.employeePhone").value("(11) 80000-0000"))
                .andExpect(jsonPath("$.employeeEmail").value("emp@email.com"))
                .andExpect(jsonPath("$.serviceItemName").value("ServiceItemName"))
                .andExpect(jsonPath("$.serviceItemDescription").value("serviceItemDescription"))
                .andExpect(jsonPath("$.serviceItemPrice").value(new BigDecimal("40")))
                .andExpect(jsonPath("$.durationMinutes").value(10))
                .andExpect(jsonPath("$.appointmentDate").value(expectedAppointmentDate))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

            verify(appointmentService).create(appointmentRequest);
    }

    @Test
    void shouldReturnBadRequestWhenCreatingAppointmentWithInvalidData() throws Exception {
        UUID clientId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID employeeId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID serviceItemId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime pastDate = LocalDate.now().minusDays(1).atTime(14, 30);

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                clientId,
                employeeId,
                serviceItemId,
                pastDate
        );

        mvc.perform(
                post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest))
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(appointmentService);
    }

    @Test
    void shouldReturnNotFoundWhenCreatingAppointmentWithNonexistentEntity() throws Exception {
        UUID clientId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID employeeId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID serviceItemId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                clientId,
                employeeId,
                serviceItemId,
                futureDate
        );

        when(appointmentService.create(appointmentRequest))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + appointmentRequest.clientId()));

        mvc.perform(
                        post("/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequest))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Client not found with ID: " + appointmentRequest.clientId()));

        verify(appointmentService).create(appointmentRequest);
    }

    @Test
    void shouldReturnConflictWhenCreatingAppointmentWithUnavailableEmployee() throws Exception {
        UUID clientId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID employeeId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID serviceItemId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                clientId,
                employeeId,
                serviceItemId,
                futureDate
        );

        when(appointmentService.create(appointmentRequest))
                .thenThrow(new EmployeeNotAvailable("Employee is inactive"));

        mvc.perform(
                post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest))
        ).andExpect(status().isConflict())
                        .andExpect(jsonPath("$.status").value(409))
                        .andExpect(jsonPath("$.error").value("Conflict"))
                        .andExpect(jsonPath("$.message").value("Employee is inactive"));

        verify(appointmentService).create(appointmentRequest);
    }

    @Test
    void shouldReturnConflictWhenCreatingAppointmentWithUnavailableServiceItem() throws Exception {
        UUID clientId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID employeeId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        UUID serviceItemId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                clientId,
                employeeId,
                serviceItemId,
                futureDate
        );

        when(appointmentService.create(appointmentRequest))
                .thenThrow(new ServiceItemNotAvailable("Service item is inactive"));

        mvc.perform(
                post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Service item is inactive"));

        verify(appointmentService).create(appointmentRequest);
    }

    @Test
    void shouldFindAppointmentById() throws Exception {
        UUID appointmentId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        String expectedAppointmentDate = futureDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );

        AppointmentResponse appointmentResponse = new AppointmentResponse(
                UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a"),
                "Firstname",
                "Lastname",
                "(11) 90000-0000",
                "client@email.com",
                "EmployeeName",
                "(11) 80000-0000",
                "emp@email.com",
                "ServiceItemName",
                "serviceItemDescription",
                new BigDecimal("40"),
                10,
                futureDate,
                AppointmentStatus.SCHEDULED
        );

        when(appointmentService.findById(appointmentId))
                .thenReturn(appointmentResponse);

        mvc.perform(
                get("/api/v1/appointments/{id}", appointmentId)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("810b60ad-e152-4656-a8f5-eb8c4d35633a"))
                .andExpect(jsonPath("$.clientFirstName").value("Firstname"))
                .andExpect(jsonPath("$.clientLastName").value("Lastname"))
                .andExpect(jsonPath("$.clientPhone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.clientEmail").value("client@email.com"))
                .andExpect(jsonPath("$.employeeName").value("EmployeeName"))
                .andExpect(jsonPath("$.employeePhone").value("(11) 80000-0000"))
                .andExpect(jsonPath("$.employeeEmail").value("emp@email.com"))
                .andExpect(jsonPath("$.serviceItemName").value("ServiceItemName"))
                .andExpect(jsonPath("$.serviceItemDescription").value("serviceItemDescription"))
                .andExpect(jsonPath("$.serviceItemPrice").value(new BigDecimal("40")))
                .andExpect(jsonPath("$.durationMinutes").value(10))
                .andExpect(jsonPath("$.appointmentDate").value(expectedAppointmentDate))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        verify(appointmentService).findById(appointmentId);
    }

    @Test
    void shouldReturnNotFoundWhenAppointmentDoesNotExist() throws Exception {
        UUID appointmentId = UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a");

        when(appointmentService.findById(appointmentId))
                .thenThrow(new EntityNotFoundException("Appointment not found with ID: " + appointmentId));

        mvc.perform(
                get("/api/v1/appointments/{id}", appointmentId)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment not found with ID: " + appointmentId));

        verify(appointmentService).findById(appointmentId);
    }

    @Test
    void shouldReturnAllAppointments() throws Exception {
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        String expectedAppointmentDate = futureDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );

        AppointmentResponse appointmentResponse = new AppointmentResponse(
                UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a"),
                "Firstname",
                "Lastname",
                "(11) 90000-0000",
                "client@email.com",
                "EmployeeName",
                "(11) 80000-0000",
                "emp@email.com",
                "ServiceItemName",
                "serviceItemDescription",
                new BigDecimal("40"),
                10,
                futureDate,
                AppointmentStatus.SCHEDULED
        );

        List<AppointmentResponse> list = List.of(appointmentResponse);
        Pageable pageable = PageRequest.of(0, 10);

        when(appointmentService.findAll(null, null, null, pageable))
                .thenReturn(new PageImpl<>(list, pageable, 1));

        mvc.perform(
                get("/api/v1/appointments")
                        .param("page", "0")
                        .param("size", "10")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("810b60ad-e152-4656-a8f5-eb8c4d35633a"))
                .andExpect(jsonPath("$.content[0].clientFirstName").value("Firstname"))
                .andExpect(jsonPath("$.content[0].clientLastName").value("Lastname"))
                .andExpect(jsonPath("$.content[0].clientPhone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.content[0].clientEmail").value("client@email.com"))
                .andExpect(jsonPath("$.content[0].employeeName").value("EmployeeName"))
                .andExpect(jsonPath("$.content[0].employeePhone").value("(11) 80000-0000"))
                .andExpect(jsonPath("$.content[0].employeeEmail").value("emp@email.com"))
                .andExpect(jsonPath("$.content[0].serviceItemName").value("ServiceItemName"))
                .andExpect(jsonPath("$.content[0].serviceItemDescription").value("serviceItemDescription"))
                .andExpect(jsonPath("$.content[0].serviceItemPrice").value(new BigDecimal("40")))
                .andExpect(jsonPath("$.content[0].durationMinutes").value(10))
                .andExpect(jsonPath("$.content[0].appointmentDate").value(expectedAppointmentDate))
                .andExpect(jsonPath("$.content[0].status").value("SCHEDULED"));

        verify(appointmentService).findAll(null, null, null, pageable);
    }

    @Test
    void shouldReturnAppointmentsWithFilters() throws Exception {
        LocalDateTime futureDate = LocalDate.now().plusDays(1).atTime(14, 30);

        String expectedAppointmentDate = futureDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );

        AppointmentResponse appointmentResponse = new AppointmentResponse(
                UUID.fromString("810b60ad-e152-4656-a8f5-eb8c4d35633a"),
                "Firstname",
                "Lastname",
                "(11) 90000-0000",
                "client@email.com",
                "EmployeeName",
                "(11) 80000-0000",
                "emp@email.com",
                "ServiceItemName",
                "serviceItemDescription",
                new BigDecimal("40"),
                10,
                futureDate,
                AppointmentStatus.SCHEDULED
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(appointmentService.findAll(AppointmentStatus.SCHEDULED, "Firstname", "EmployeeName", pageable))
                .thenReturn(new PageImpl<>(List.of(appointmentResponse), pageable, 1));

        mvc.perform(
                get("/api/v1/appointments")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "SCHEDULED")
                        .param("clientName", "Firstname")
                        .param("employeeName", "EmployeeName")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("810b60ad-e152-4656-a8f5-eb8c4d35633a"))
                .andExpect(jsonPath("$.content[0].clientFirstName").value("Firstname"))
                .andExpect(jsonPath("$.content[0].clientLastName").value("Lastname"))
                .andExpect(jsonPath("$.content[0].clientPhone").value("(11) 90000-0000"))
                .andExpect(jsonPath("$.content[0].clientEmail").value("client@email.com"))
                .andExpect(jsonPath("$.content[0].employeeName").value("EmployeeName"))
                .andExpect(jsonPath("$.content[0].employeePhone").value("(11) 80000-0000"))
                .andExpect(jsonPath("$.content[0].employeeEmail").value("emp@email.com"))
                .andExpect(jsonPath("$.content[0].serviceItemName").value("ServiceItemName"))
                .andExpect(jsonPath("$.content[0].serviceItemDescription").value("serviceItemDescription"))
                .andExpect(jsonPath("$.content[0].serviceItemPrice").value(new BigDecimal("40")))
                .andExpect(jsonPath("$.content[0].durationMinutes").value(10))
                .andExpect(jsonPath("$.content[0].appointmentDate").value(expectedAppointmentDate))
                .andExpect(jsonPath("$.content[0].status").value("SCHEDULED"));

        verify(appointmentService).findAll(AppointmentStatus.SCHEDULED, "Firstname", "EmployeeName", pageable);
    }
}