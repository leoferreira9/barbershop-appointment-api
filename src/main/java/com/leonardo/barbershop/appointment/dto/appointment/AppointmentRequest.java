package com.leonardo.barbershop.appointment.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(

        @Schema(description = "Client ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        @NotNull
        UUID clientId,

        @Schema(description = "Employee ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        @NotNull
        UUID employeeId,

        @Schema(description = "Service item ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        @NotNull
        UUID serviceItemId,

        @Schema(description = "Appointment date", example = "2026-07-20T14:30:00")
        @NotNull
        @Future
        LocalDateTime appointmentDate
) {}
