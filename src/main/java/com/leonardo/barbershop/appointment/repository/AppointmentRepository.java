package com.leonardo.barbershop.appointment.repository;

import com.leonardo.barbershop.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {}
