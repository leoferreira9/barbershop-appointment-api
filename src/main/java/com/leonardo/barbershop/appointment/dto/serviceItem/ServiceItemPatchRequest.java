package com.leonardo.barbershop.appointment.dto.serviceItem;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ServiceItemPatchRequest(
        @Size(max = 150)
        String name,

        @Size(max = 200)
        String description,

        @Positive
        @Digits(integer = 6, fraction = 2)
        BigDecimal price,

        @Min(value = 10)
        @Max(value = 90)
        Integer durationMinutes
) {}
