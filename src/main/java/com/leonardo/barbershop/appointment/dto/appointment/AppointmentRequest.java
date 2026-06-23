package com.leonardo.barbershop.appointment.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentRequest {

    @NotNull
    private UUID clientId;

    @NotNull
    private UUID employeeId;

    @NotNull
    private UUID serviceItemId;

    @NotNull
    @Future
    private LocalDateTime appointmentDate;
}
