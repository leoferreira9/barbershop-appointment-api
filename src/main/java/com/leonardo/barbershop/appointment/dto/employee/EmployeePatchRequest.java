package com.leonardo.barbershop.appointment.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeePatchRequest(
        @Size(max = 150)
        String name,

        @Size(max = 20)
        @Pattern(regexp = "^(?:\\+?55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$")
        String phone,

        @Email
        @Size(max = 256)
        String email
) {}
