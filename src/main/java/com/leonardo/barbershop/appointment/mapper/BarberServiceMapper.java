package com.leonardo.barbershop.appointment.mapper;

import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceRequest;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceResponse;
import com.leonardo.barbershop.appointment.model.BarberService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarberServiceMapper {

    BarberServiceResponse toDto(BarberService barberService);

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "id", ignore = true)
    BarberService toEntity(BarberServiceRequest request);

}
