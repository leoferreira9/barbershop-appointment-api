package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.client.ClientRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.dto.client.ClientUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.mapper.ClientMapper;
import com.leonardo.barbershop.appointment.model.Client;
import com.leonardo.barbershop.appointment.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientService(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private Client findClientByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + id));
    }

    private void updateClientData(ClientUpdateRequest request, Client client){
        client.setBirthDate(request.getBirthDate());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
    }

    @Transactional
    public ClientResponse create(ClientRequest request){
        if(repository.findByEmail(request.getEmail()).isPresent())
            throw new EmailAlreadyRegisteredException("Email " + request.getEmail() + " already registered!");

        Client client = mapper.toEntity(request);
        Client savedClient = repository.save(client);
        return mapper.toDto(savedClient);
    }

    public ClientResponse findById(UUID id){
        Client clientExists = findClientByIdOrThrow(id);
        return mapper.toDto(clientExists);
    }

    public Page<ClientResponse> findAll(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public ClientResponse update(UUID id, ClientUpdateRequest request){
        Client clientExists = findClientByIdOrThrow(id);
        Optional<Client> emailAlreadyRegistered = repository.findByEmail(request.getEmail());

        if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(clientExists.getId()))
            throw new EmailAlreadyRegisteredException("Email " + request.getEmail() + " already registered!");

        updateClientData(request, clientExists);
        Client savedClient = repository.save(clientExists);
        return mapper.toDto(savedClient);
    }

    @Transactional
    public ClientResponse deactivate(UUID id){
        Client clientExists = findClientByIdOrThrow(id);
        if(!clientExists.isActive())
            throw new EntityAlreadyDeactivatedException("Client already deactivated");

        clientExists.setActive(false);
        Client savedClient = repository.save(clientExists);
        return mapper.toDto(savedClient);
    }

    @Transactional
    public ClientResponse activate(UUID id){
        Client clientExists = findClientByIdOrThrow(id);
        if(clientExists.isActive())
            throw new EntityAlreadyActivatedException("Client already activated");
        clientExists.setActive(true);
        Client savedClient = repository.save(clientExists);
        return mapper.toDto(savedClient);
    }
}
