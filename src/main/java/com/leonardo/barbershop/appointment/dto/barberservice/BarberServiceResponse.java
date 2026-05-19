package com.leonardo.barbershop.appointment.dto.barberservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BarberServiceResponse {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int durationMinutes;
    private boolean active;
}
