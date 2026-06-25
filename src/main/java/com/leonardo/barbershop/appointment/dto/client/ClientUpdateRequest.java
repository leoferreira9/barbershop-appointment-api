package com.leonardo.barbershop.appointment.dto.client;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClientUpdateRequest(
        @NotBlank
        @Size(max = 50)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotBlank
        @Email
        @Size(max = 256)
        String email,

        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^(?:\\+?55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$")
        String phone,

        @NotNull
        @Past
        LocalDate birthDate
) {
}
