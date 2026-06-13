package com.leonardo.barbershop.appointment.repository;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByEmployee_IdAndAppointmentDateAndStatusNot(
            UUID employeeId,
            LocalDateTime appointmentDate,
            AppointmentStatus status
    );
}
