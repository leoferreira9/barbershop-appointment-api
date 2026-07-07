package com.leonardo.barbershop.appointment.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeResponse(

        @Schema(description = "Employee ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        UUID id,

        @Schema(description = "Employee name", example = "Laura")
        String name,

        @Schema(description = "Employee phone number", example = "(00) 00000-0000")
        String phone,

        @Schema(description = "Employee email", example = "laura@email.com")
        String email,

        @Schema(description = "Employee active", example = "true")
        boolean active
) {}
