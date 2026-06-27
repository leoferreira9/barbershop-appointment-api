package com.leonardo.barbershop.appointment.dto.serviceItem;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ServiceItemRequest(
        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 200)
        String description,

        @NotNull
        @Positive
        @Digits(integer = 6, fraction = 2)
        BigDecimal price,

        @NotNull
        @Min(value = 10)
        @Max(value = 90)
        Integer durationMinutes
) {}
