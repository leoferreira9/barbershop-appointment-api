package com.leonardo.barbershop.appointment.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeResponse {

    private UUID id;
    private String name;
    private String phone;
    private String email;
    private boolean active;
}
