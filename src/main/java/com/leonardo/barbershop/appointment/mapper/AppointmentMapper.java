package com.leonardo.barbershop.appointment.mapper;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "client.firstName", target = "clientFirstName")
    @Mapping(source = "client.lastName", target = "clientLastName")
    @Mapping(source = "client.email", target = "clientEmail")
    @Mapping(source = "client.phone", target = "clientPhone")
    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "employee.phone", target = "employeePhone")
    @Mapping(source = "employee.email", target = "employeeEmail")
    @Mapping(source = "barberService.name", target = "barberServiceName")
    @Mapping(source = "barberService.description", target = "barberServiceDescription")
    @Mapping(source = "barberService.price", target = "barberServicePrice")
    @Mapping(source = "barberService.durationMinutes", target = "durationMinutes")
    AppointmentResponse toDto(Appointment appointment);

}
