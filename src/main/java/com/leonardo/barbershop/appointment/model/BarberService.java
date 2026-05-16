package com.leonardo.barbershop.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Table(name = "barber_services")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class BarberService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private boolean active;

    public BarberService(String name, String description, BigDecimal price, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BarberService barberService = (BarberService) o;
        if(this.id == null || barberService.id == null) return false;
        return Objects.equals(getId(), barberService.getId());
    }

    @Override
    public int hashCode() {
        if(this.id == null) return 0;
        return Objects.hashCode(getId());
    }
}
