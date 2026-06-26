package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.filters.EmployeeFilter;
import com.leonardo.barbershop.appointment.mapper.EmployeeMapper;
import com.leonardo.barbershop.appointment.model.Employee;
import com.leonardo.barbershop.appointment.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    private Employee findEmployeeByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
    }

    private void updateEmployeeData(EmployeeUpdateRequest request, Employee employee){
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request){
        if(repository.findByEmail(request.email()).isPresent())
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");

        Employee employee = mapper.toEntity(request);
        Employee savedEmployee = repository.save(employee);
        return mapper.toDto(savedEmployee);
    }

    public EmployeeResponse findById(UUID id){
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        return mapper.toDto(employeeExists);
    }

    public Page<EmployeeResponse> findAll(String name, Boolean active, Pageable pageable){

        Specification<Employee> specification = Specification
                .where(EmployeeFilter.hasName(name))
                .and(EmployeeFilter.hasActive(active));

        return repository.findAll(specification, pageable).map(mapper::toDto);
    }

    @Transactional
    public EmployeeResponse update(UUID id, EmployeeUpdateRequest request){
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        Optional<Employee> emailAlreadyRegistered = repository.findByEmail(request.email());
        if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(employeeExists.getId()))
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");

        updateEmployeeData(request, employeeExists);
        Employee savedEmployee = repository.save(employeeExists);
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponse deactivate(UUID id){
        Employee employeeExists = findEmployeeByIdOrThrow(id);

        if(!employeeExists.isActive())
            throw new EntityAlreadyDeactivatedException("Employee already deactivated");

        employeeExists.setActive(false);
        Employee savedEmployee = repository.save(employeeExists);
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponse activate(UUID id){
        Employee employeeExists = findEmployeeByIdOrThrow(id);

        if(employeeExists.isActive())
            throw new EntityAlreadyActivatedException("Employee already activated");

        employeeExists.setActive(true);
        Employee savedEmployee = repository.save(employeeExists);
        return mapper.toDto(savedEmployee);
    }
}
