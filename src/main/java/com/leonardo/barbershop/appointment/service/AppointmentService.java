package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentRequest;
import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.exception.*;
import com.leonardo.barbershop.appointment.filters.AppointmentFilter;
import com.leonardo.barbershop.appointment.mapper.AppointmentMapper;
import com.leonardo.barbershop.appointment.model.Appointment;
import com.leonardo.barbershop.appointment.model.ServiceItem;
import com.leonardo.barbershop.appointment.model.Client;
import com.leonardo.barbershop.appointment.model.Employee;
import com.leonardo.barbershop.appointment.repository.AppointmentRepository;
import com.leonardo.barbershop.appointment.repository.ServiceItemRepository;
import com.leonardo.barbershop.appointment.repository.ClientRepository;
import com.leonardo.barbershop.appointment.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceItemRepository serviceItemRepository;

    public AppointmentService(AppointmentRepository repository, AppointmentMapper mapper, ClientRepository clientRepository, EmployeeRepository employeeRepository, ServiceItemRepository serviceItemRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    private Appointment findAppointmentByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + id));
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request){
        Client clientExists = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + request.clientId()));

        Employee employeeExists = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + request.employeeId()));

        ServiceItem serviceItemExists = serviceItemRepository.findById(request.serviceItemId())
                .orElseThrow(() -> new EntityNotFoundException("Service item not found with ID: " + request.serviceItemId()));

        if(!employeeExists.isActive())
            throw new EmployeeNotAvailable("Employee is inactive");

        if(!serviceItemExists.isActive())
            throw new ServiceItemNotAvailable("Service item is inactive");

        if(!request.appointmentDate().isAfter(LocalDateTime.now()))
            throw new DateNotValidException("Appointment date must not be in the past");

        if(repository.existsByEmployee_IdAndAppointmentDateAndStatusNot(request.employeeId(), request.appointmentDate(), AppointmentStatus.CANCELED))
            throw new EmployeeNotAvailable("Employee not available on this date");

        Appointment appointment = new Appointment(clientExists, employeeExists, serviceItemExists, request.appointmentDate());
        Appointment savedAppointment = repository.save(appointment);
        return mapper.toDto(savedAppointment);
    }

    public AppointmentResponse findById(UUID id){
        Appointment appointmentExists = findAppointmentByIdOrThrow(id);
        return mapper.toDto(appointmentExists);
    }

    public Page<AppointmentResponse> findAll(AppointmentStatus status, String clientName, String employeeName, Pageable pageable){

        Specification<Appointment> specification = Specification.where(AppointmentFilter.hasStatus(status))
                .and(AppointmentFilter.hasClientName(clientName))
                .and(AppointmentFilter.hasEmployeeName(employeeName));

        return repository.findAll(specification, pageable).map(mapper::toDto);
    }

    @Transactional
    public AppointmentResponse cancel(UUID id){
        Appointment appointmentExists = findAppointmentByIdOrThrow(id);

        if(appointmentExists.getStatus() == AppointmentStatus.CANCELED)
            throw new EntityAlreadyDeactivatedException("Appointment already canceled");

        appointmentExists.setStatus(AppointmentStatus.CANCELED);
        Appointment savedAppointment = repository.save(appointmentExists);
        return mapper.toDto(savedAppointment);
    }

    @Transactional
    public AppointmentResponse complete(UUID id){
        Appointment appointmentExists = findAppointmentByIdOrThrow(id);

        if(appointmentExists.getStatus() == AppointmentStatus.COMPLETED)
            throw new EntityAlreadyCompleted("Appointment already completed");

        appointmentExists.setStatus(AppointmentStatus.COMPLETED);
        Appointment savedAppointment = repository.save(appointmentExists);
        return mapper.toDto(savedAppointment);
    }
}
