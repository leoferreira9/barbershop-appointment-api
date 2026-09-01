package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentRequest;
import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
}