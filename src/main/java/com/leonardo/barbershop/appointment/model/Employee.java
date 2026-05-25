package com.leonardo.barbershop.appointment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Table(name = "employees")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, unique = true, length = 256)
    private String email;

    @Column(nullable = false)
    private boolean active;

    public Employee(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        if(this.id == null || employee.id == null) return false;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        if(this.id == null) return 0;
        return Objects.hashCode(getId());
    }
}
