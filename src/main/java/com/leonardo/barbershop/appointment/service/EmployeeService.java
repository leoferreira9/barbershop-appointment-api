package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.mapper.EmployeeMapper;
import com.leonardo.barbershop.appointment.model.Employee;
import com.leonardo.barbershop.appointment.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request){
        if(repository.findByEmail(request.getEmail()).isPresent())
            throw new EmailAlreadyRegisteredException("Email " + request.getEmail() + " already registered!");

        Employee employee = mapper.toEntity(request);
        Employee savedEmployee = repository.save(employee);
        return mapper.toDto(savedEmployee);
    }

    public EmployeeResponse findById(UUID id){
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        return mapper.toDto(employeeExists);
    }

    public List<EmployeeResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public EmployeeResponse update(UUID id, EmployeeUpdateRequest request){
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        Optional<Employee> emailAlreadyRegistered = repository.findByEmail(request.getEmail());
        if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(employeeExists.getId()))
            throw new EmailAlreadyRegisteredException("Email " + request.getEmail() + " already registered!");

        updateEmployeeData(request, employeeExists);
        Employee savedEmployee = repository.save(employeeExists);
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public void delete(UUID id){
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        repository.delete(employeeExists);
    }
}
