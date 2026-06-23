package com.leonardo.barbershop.appointment.repository;

import com.leonardo.barbershop.appointment.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, UUID>, JpaSpecificationExecutor<ServiceItem> {}
