package com.leonardo.barbershop.appointment.mapper;

import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemRequest;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemResponse;
import com.leonardo.barbershop.appointment.model.ServiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {

    ServiceItemResponse toDto(ServiceItem serviceItem);

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "id", ignore = true)
    ServiceItem toEntity(ServiceItemRequest request);

}
