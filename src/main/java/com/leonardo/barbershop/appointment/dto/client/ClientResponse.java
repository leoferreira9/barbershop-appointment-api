package com.leonardo.barbershop.appointment.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponse(

        @Schema(description = "Client ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        UUID id,

        @Schema(description = "Client first name", example = "Leonardo")
        String firstName,

        @Schema(description = "Client last name", example = "Barros")
        String lastName,

        @Schema(description = "Client email", example = "leo@email.com")
        String email,

        @Schema(description = "Client phone number", example = "(00) 00000-0000")
        String phone,

        @Schema(description = "Client birth date", example = "1999-02-04")
        LocalDate birthDate,

        @Schema(description = "Client is active", example = "true")
        boolean active
) {}
