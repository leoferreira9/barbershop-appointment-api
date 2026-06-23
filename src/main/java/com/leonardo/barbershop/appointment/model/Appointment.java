package com.leonardo.barbershop.appointment.model;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table(name = "appointments")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_item_id", nullable = false)
    private ServiceItem serviceItem;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    public Appointment(Client client, Employee employee, ServiceItem serviceItem, LocalDateTime appointmentDate) {
        this.client = client;
        this.employee = employee;
        this.serviceItem = serviceItem;
        this.appointmentDate = appointmentDate;
        this.status = AppointmentStatus.SCHEDULED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Appointment appointment = (Appointment) o;
        if(this.id == null || appointment.id == null) return false;
        return Objects.equals(getId(), appointment.getId());
    }

    @Override
    public int hashCode() {
        if(this.id == null) return 0;
        return Objects.hashCode(getId());
    }
}
