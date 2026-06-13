package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentRequest;
import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.exception.DateNotValidException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyCompleted;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.mapper.AppointmentMapper;
import com.leonardo.barbershop.appointment.model.Appointment;
import com.leonardo.barbershop.appointment.model.BarberService;
import com.leonardo.barbershop.appointment.model.Client;
import com.leonardo.barbershop.appointment.model.Employee;
import com.leonardo.barbershop.appointment.repository.AppointmentRepository;
import com.leonardo.barbershop.appointment.repository.BarberServiceRepository;
import com.leonardo.barbershop.appointment.repository.ClientRepository;
import com.leonardo.barbershop.appointment.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BarberServiceRepository barberServiceRepository;

    public AppointmentService(AppointmentRepository repository, AppointmentMapper mapper, ClientRepository clientRepository, EmployeeRepository employeeRepository, BarberServiceRepository barberServiceRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.barberServiceRepository = barberServiceRepository;
    }

    private Appointment findAppointmentByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + id));
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request){
        Client clientExists = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + request.getClientId()));

        Employee employeeExists = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + request.getEmployeeId()));

        BarberService barberServiceExists = barberServiceRepository.findById(request.getBarberServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Barber service not found with ID: " + request.getBarberServiceId()));

        if(!request.getAppointmentDate().isAfter(LocalDateTime.now()))
            throw new DateNotValidException("Appointment date must not be in the past");

        Appointment appointment = new Appointment(clientExists, employeeExists, barberServiceExists, request.getAppointmentDate());
        Appointment savedAppointment = repository.save(appointment);
        return mapper.toDto(savedAppointment);
    }

    public AppointmentResponse findById(UUID id){
        Appointment appointmentExists = findAppointmentByIdOrThrow(id);
        return mapper.toDto(appointmentExists);
    }

    public List<AppointmentResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
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
