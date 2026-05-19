package com.leonardo.barbershop.appointment.dto.appointment;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppointmentResponse {

    private UUID id;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private String clientEmail;

    private String employeeName;
    private String employeePhone;
    private String employeeEmail;

    private String barberServiceName;
    private String barberServiceDescription;
    private BigDecimal barberServicePrice;
    private Integer durationMinutes;

    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
}
