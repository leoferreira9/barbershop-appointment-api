package com.leonardo.barbershop.appointment.dto.serviceItem;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        int durationMinutes,
        boolean active
) {}
