package com.leonardo.barbershop.appointment.model;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "appointments")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "barber_service_id", nullable = false)
    private BarberService barberService;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    public Appointment(Client client, Employee employee, BarberService barberService, LocalDateTime appointmentDate) {
        this.client = client;
        this.employee = employee;
        this.barberService = barberService;
        this.appointmentDate = appointmentDate;
        this.status = AppointmentStatus.SCHEDULED;
    }
}
