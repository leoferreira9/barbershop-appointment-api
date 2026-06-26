package com.leonardo.barbershop.appointment.dto.employee;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String name,
        String phone,
        String email,
        boolean active
) {}
