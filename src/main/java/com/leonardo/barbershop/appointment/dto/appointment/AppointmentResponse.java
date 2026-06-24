package com.leonardo.barbershop.appointment.dto.appointment;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String clientFirstName,
        String clientLastName,
        String clientPhone,
        String clientEmail,
        String employeeName,
        String employeePhone,
        String employeeEmail,
        String serviceItemName,
        String serviceItemDescription,
        BigDecimal serviceItemPrice,
        Integer durationMinutes,
        LocalDateTime appointmentDate,
        AppointmentStatus status
){}
