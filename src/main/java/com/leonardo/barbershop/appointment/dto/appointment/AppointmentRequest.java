package com.leonardo.barbershop.appointment.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull
        UUID clientId,

        @NotNull
        UUID employeeId,

        @NotNull
        UUID serviceItemId,

        @NotNull
        @Future
        LocalDateTime appointmentDate
) {}
