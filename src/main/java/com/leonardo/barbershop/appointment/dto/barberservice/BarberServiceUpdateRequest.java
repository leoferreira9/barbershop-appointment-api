package com.leonardo.barbershop.appointment.dto.barberservice;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BarberServiceUpdateRequest {
    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    @NotNull
    @Min(value = 10)
    @Max(value = 90)
    private Integer durationMinutes;
}
