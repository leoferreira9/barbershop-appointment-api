package com.leonardo.barbershop.appointment.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(

        @Schema(description = "Employee name", example = "Laura")
        @NotBlank
        @Size(max = 150)
        String name,


        @Schema(description = "Employee phone number", example = "(00) 00000-0000")
        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^(?:\\+?55\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$")
        String phone,

        @Schema(description = "Employee email", example = "laura@email.com")
        @NotBlank
        @Email
        @Size(max = 256)
        String email
) {}
