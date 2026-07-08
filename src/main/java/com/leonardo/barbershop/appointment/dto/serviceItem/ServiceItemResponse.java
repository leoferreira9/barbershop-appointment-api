package com.leonardo.barbershop.appointment.dto.serviceItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceItemResponse(

        @Schema(description = "Service item ID", example = "810b60ad-e152-4656-a8f5-eb8c4d35633a")
        UUID id,

        @Schema(description = "Service item name", example = "Cabelo + Barba")
        String name,

        @Schema(description = "Service item description", example = "Corte de cabelo masculino + barba detalhada")
        String description,

        @Schema(description = "Service item price", example = "70.00")
        BigDecimal price,

        @Schema(description = "Service item duration in minutes", example = "30")
        int durationMinutes,

        @Schema(description = "Service item active", example = "true")
        boolean active
) {}
