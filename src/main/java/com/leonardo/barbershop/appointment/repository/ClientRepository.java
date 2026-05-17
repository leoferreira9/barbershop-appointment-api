package com.leonardo.barbershop.appointment.repository;

import com.leonardo.barbershop.appointment.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {}
