package com.leonardo.barbershop.appointment.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClientPatchRequest(

        @Schema(description = "Client first name", example = "Leonardo")
        @Size(max = 50)
        String firstName,

        @Schema(description = "Client last name", example = "Barros")
        @Size(max = 100)
        String lastName,

        @Schema(description = "Client email", example = "leo@email.com")
        @Email
        @Size(max = 256)
        String email,

        @Schema(description = "Client phone number", example = "(00) 00000-0000")
        @Size(max = 20)
        @Pattern(regexp = "^(?:\\+?55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$")
        String phone,

        @Schema(description = "Client birth date", example = "1999-02-04")
        @Past
        LocalDate birthDate
){}
