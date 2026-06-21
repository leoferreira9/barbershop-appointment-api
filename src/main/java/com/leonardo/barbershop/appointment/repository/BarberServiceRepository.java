package com.leonardo.barbershop.appointment.repository;

import com.leonardo.barbershop.appointment.model.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BarberServiceRepository extends JpaRepository<BarberService, UUID>, JpaSpecificationExecutor<BarberService> {}
