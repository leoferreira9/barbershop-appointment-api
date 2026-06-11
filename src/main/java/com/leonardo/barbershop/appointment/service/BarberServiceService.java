package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceRequest;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceResponse;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.mapper.BarberServiceMapper;
import com.leonardo.barbershop.appointment.model.BarberService;
import com.leonardo.barbershop.appointment.repository.BarberServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BarberServiceService {

    private final BarberServiceRepository repository;
    private final BarberServiceMapper mapper;

    public BarberServiceService(BarberServiceRepository repository, BarberServiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private BarberService findBarberServiceByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service not found with ID: " + id));
    }

    private void updateBarberServiceData(BarberService barberService, BarberServiceUpdateRequest request){
        barberService.setName(request.getName());
        barberService.setPrice(request.getPrice());
        barberService.setDescription(request.getDescription());
        barberService.setDurationMinutes(request.getDurationMinutes());
    }

    @Transactional
    public BarberServiceResponse create(BarberServiceRequest request){
        BarberService barberService = mapper.toEntity(request);
        BarberService savedBarberService = repository.save(barberService);
        return mapper.toDto(savedBarberService);
    }

    public BarberServiceResponse findById(UUID id){
        BarberService barberServiceExists = findBarberServiceByIdOrThrow(id);
        return mapper.toDto(barberServiceExists);
    }

    public List<BarberServiceResponse> findAll(){
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public BarberServiceResponse update(UUID id, BarberServiceUpdateRequest request){
        BarberService barberServiceExists = findBarberServiceByIdOrThrow(id);
        updateBarberServiceData(barberServiceExists, request);
        BarberService savedBarberService = repository.save(barberServiceExists);
        return mapper.toDto(savedBarberService);
    }

    @Transactional
    public BarberServiceResponse deactivate(UUID id){
        BarberService barberServiceExists = findBarberServiceByIdOrThrow(id);

        if(!barberServiceExists.isActive())
            throw new EntityAlreadyDeactivatedException("Barber service already deactivated");

        barberServiceExists.setActive(false);
        BarberService savedBarberService = repository.save(barberServiceExists);
        return mapper.toDto(savedBarberService);
    }

    @Transactional
    public BarberServiceResponse activate(UUID id){
        BarberService barberServiceExists = findBarberServiceByIdOrThrow(id);

        if(barberServiceExists.isActive())
            throw new EntityAlreadyActivatedException("Barber service already activated");

        barberServiceExists.setActive(true);
        BarberService savedBarberService = repository.save(barberServiceExists);
        return mapper.toDto(savedBarberService);
    }
}
