package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.employee.EmployeePatchRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    private void patchEmployeeData(Employee employee, EmployeePatchRequest request){
        if(request.email() != null && !request.email().isBlank()){
            employee.setEmail(request.email());
        }

        if(request.name() != null && !request.name().isBlank()){
            employee.setName(request.name());
        }

        if(request.phone() != null && !request.phone().isBlank()){
            employee.setPhone(request.phone());
        }
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request){
        if(repository.findByEmail(request.email()).isPresent()){
            log.warn("Email already registered: {}", request.email());
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        log.info("Creating employee with email: {}", request.email());

        Employee employee = mapper.toEntity(request);
        Employee savedEmployee = repository.save(employee);

        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return mapper.toDto(savedEmployee);
    }

    public EmployeeResponse findById(UUID id){
        log.info("Finding employee with ID: {}", id);
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        log.info("Employee with ID: {} successfully found", id);
        return mapper.toDto(employeeExists);
    }

    public Page<EmployeeResponse> findAll(String name, Boolean active, Pageable pageable){
        log.info("Finding all employees");
        Specification<Employee> specification = Specification
                .where(EmployeeFilter.hasName(name))
                .and(EmployeeFilter.hasActive(active));

        Page<EmployeeResponse> employees = repository.findAll(specification, pageable).map(mapper::toDto);
        log.info("Found: {} employees", employees.getTotalElements());
        return employees;
    }

    @Transactional
    public EmployeeResponse update(UUID id, EmployeeUpdateRequest request){
        log.info("Updating employee with ID: {} (PUT)", id);
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        Optional<Employee> emailAlreadyRegistered = repository.findByEmail(request.email());
        if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(employeeExists.getId())){
            log.warn("Email already registered: {}", request.email());
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        updateEmployeeData(request, employeeExists);
        Employee savedEmployee = repository.save(employeeExists);
        log.info("Employee with ID: {} successfully updated", savedEmployee.getId());
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponse partialUpdate(UUID id, EmployeePatchRequest request){
        log.info("Updating employee with ID: {} (PATCH)", id);
        Employee employeeExists = findEmployeeByIdOrThrow(id);
        if(request.email() != null && !request.email().isBlank()){
            Optional<Employee> emailAlreadyRegistered = repository.findByEmail(request.email());
            if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(employeeExists.getId())){
                log.warn("Email already registered: {}", request.email());
                throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
            }
        }

        patchEmployeeData(employeeExists, request);
        Employee savedEmployee = repository.save(employeeExists);
        log.info("Employee with ID: {} partially updated", savedEmployee.getId());
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponse deactivate(UUID id){
        log.info("Deactivating employee with ID: {}", id);
        Employee employeeExists = findEmployeeByIdOrThrow(id);

        if(!employeeExists.isActive()){
            log.warn("Employee already deactivated with ID: {}", id);
            throw new EntityAlreadyDeactivatedException("Employee already deactivated");
        }

        employeeExists.setActive(false);
        Employee savedEmployee = repository.save(employeeExists);
        log.info("Employee with ID: {} successfully deactivated", savedEmployee.getId());
        return mapper.toDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponse activate(UUID id){
        log.info("Activating employee with ID: {}", id);
        Employee employeeExists = findEmployeeByIdOrThrow(id);

        if(employeeExists.isActive()){
            log.warn("Employee already activated with ID: {}", id);
            throw new EntityAlreadyActivatedException("Employee already activated");
        }

        employeeExists.setActive(true);
        Employee savedEmployee = repository.save(employeeExists);
        log.info("Employee with ID: {} successfully activated", savedEmployee.getId());
        return mapper.toDto(savedEmployee);
    }
}
