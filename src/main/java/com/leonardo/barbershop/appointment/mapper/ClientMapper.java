package com.leonardo.barbershop.appointment.mapper;

import com.leonardo.barbershop.appointment.dto.client.ClientRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponse toDto(Client client);

    @Mapping(target = "id", ignore = true)
    Client toEntity (ClientRequest request);

}
