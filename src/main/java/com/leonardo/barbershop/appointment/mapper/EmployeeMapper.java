package com.leonardo.barbershop.appointment.mapper;

import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponse toDto(Employee employee);

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequest request);

}
