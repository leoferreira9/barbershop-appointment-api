package com.leonardo.barbershop.appointment.dto.serviceItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ServiceItemUpdateRequest(

        @Schema(description = "Service item name", example = "Cabelo + Barba")
        @NotBlank
        @Size(max = 150)
        String name,

        @Schema(description = "Service item description", example = "Corte de cabelo masculino + barba detalhada")
        @Size(max = 200)
        String description,

        @Schema(description = "Service item price", example = "70.00")
        @NotNull
        @Positive
        @Digits(integer = 6, fraction = 2)
        BigDecimal price,

        @Schema(description = "Service item duration in minutes", example = "30")
        @NotNull
        @Min(value = 10)
        @Max(value = 90)
        Integer durationMinutes
) {}
