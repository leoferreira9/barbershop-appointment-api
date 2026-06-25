package com.leonardo.barbershop.appointment.dto.client;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        boolean active
) {}
