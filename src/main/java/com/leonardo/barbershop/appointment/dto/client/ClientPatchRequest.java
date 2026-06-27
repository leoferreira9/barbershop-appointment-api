package com.leonardo.barbershop.appointment.dto.client;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClientPatchRequest(

        @Size(max = 50)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Email
        @Size(max = 256)
        String email,

        @Size(max = 20)
        @Pattern(regexp = "^(?:\\+?55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$")
        String phone,

        @Past
        LocalDate birthDate
){}
