package com.leonardo.barbershop.appointment.dto.appointment;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        @Schema(description = "Appointment ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        UUID id,

        @Schema(description = "Client first name", example = "Leonardo")
        String clientFirstName,

        @Schema(description = "Client last name", example = "Barros")
        String clientLastName,

        @Schema(description = "Client phone number", example = "(99) 9999-9999")
        String clientPhone,

        @Schema(description = "Client email", example = "leo@email.com")
        String clientEmail,

        @Schema(description = "Employee name", example = "Laura")
        String employeeName,

        @Schema(description = "Employee phone number", example = "(99) 9999-9999")
        String employeePhone,

        @Schema(description = "Employee email", example = "laura@email.com")
        String employeeEmail,

        @Schema(description = "Service item name", example = "Corte + Barba")
        String serviceItemName,

        @Schema(description = "Service item description", example = "Corte masculino completo com barba")
        String serviceItemDescription,

        @Schema(description = "Service item price", example = "75.00")
        BigDecimal serviceItemPrice,

        @Schema(description = "Appointment minutes duration", example = "30")
        Integer durationMinutes,

        @Schema(description = "Appointment date", example = "2026-07-20T14:30:00")
        LocalDateTime appointmentDate,

        @Schema(description = "Appointment status", example = "COMPLETED")
        AppointmentStatus status
){}
