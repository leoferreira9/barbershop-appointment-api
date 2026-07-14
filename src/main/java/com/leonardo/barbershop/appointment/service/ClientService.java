package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.client.ClientPatchRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.dto.client.ClientUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.filters.ClientFilter;
import com.leonardo.barbershop.appointment.mapper.ClientMapper;
import com.leonardo.barbershop.appointment.model.Client;
import com.leonardo.barbershop.appointment.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
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
        client.setBirthDate(request.birthDate());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());
    }

    private void patchClientData(ClientPatchRequest request, Client client){
        if(request.firstName() != null && !request.firstName().isBlank()){
            client.setFirstName(request.firstName());
        }

        if(request.lastName() != null && !request.lastName().isBlank()){
            client.setLastName(request.lastName());
        }

        if(request.email() != null && !request.email().isBlank()){
            client.setEmail(request.email());
        }

        if(request.phone() != null && !request.phone().isBlank()){
            client.setPhone(request.phone());
        }

        if(request.birthDate() != null){
            client.setBirthDate(request.birthDate());
        }
    }

    @Transactional
    public ClientResponse create(ClientRequest request){
        if(repository.findByEmail(request.email()).isPresent()){
            log.warn("Email already registered: {}", request.email());
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        log.info("Creating client with email: {}", request.email());

        Client client = mapper.toEntity(request);
        Client savedClient = repository.save(client);

        log.info("Client created successfully with ID: {}", savedClient.getId());
        return mapper.toDto(savedClient);
    }

    public ClientResponse findById(UUID id){
        log.info("Finding client with ID: {}", id);
        Client clientExists = findClientByIdOrThrow(id);
        log.info("Client with ID: {} successfully found", id);
        return mapper.toDto(clientExists);
    }

    public Page<ClientResponse> findAll(String name, Boolean active, Pageable pageable){
        log.info("Finding all clients");
        Specification<Client> specification = Specification
                .where(ClientFilter.hasName(name))
                .and(ClientFilter.hasActive(active));

        Page<ClientResponse> clients = repository.findAll(specification, pageable).map(mapper::toDto);
        log.info("Found: {} clients", clients.getTotalElements());
        return clients;
    }

    @Transactional
    public ClientResponse update(UUID id, ClientUpdateRequest request){
        log.info("Updating client with ID: {} (PUT)", id);
        Client clientExists = findClientByIdOrThrow(id);
        Optional<Client> emailAlreadyRegistered = repository.findByEmail(request.email());

        if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(clientExists.getId())){
            log.warn("Email already registered: {}", request.email());
            throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
        }

        updateClientData(request, clientExists);
        Client savedClient = repository.save(clientExists);

        log.info("Client with ID: {} successfully updated", savedClient.getId());
        return mapper.toDto(savedClient);
    }

    @Transactional
    public ClientResponse partialUpdate(UUID id, ClientPatchRequest request){
        log.info("Updating client with ID: {} (PATCH)", id);
        Client clientExists = findClientByIdOrThrow(id);
        if(request.email() != null && !request.email().isBlank()){
            Optional<Client> emailAlreadyRegistered = repository.findByEmail(request.email());
            if(emailAlreadyRegistered.isPresent() && !emailAlreadyRegistered.get().getId().equals(clientExists.getId())){
                log.warn("Email already registered: {}", request.email());
                throw new EmailAlreadyRegisteredException("Email " + request.email() + " already registered!");
            }
        }

        patchClientData(request, clientExists);
        Client savedClient = repository.save(clientExists);

        log.info("Client with ID: {} partially updated", savedClient.getId());

        return mapper.toDto(savedClient);
    }

    @Transactional
    public ClientResponse deactivate(UUID id){
        log.info("Deactivating client with ID: {}", id);
        Client clientExists = findClientByIdOrThrow(id);
        if(!clientExists.isActive()){
            log.warn("Client already deactivated with ID: {}", id);
            throw new EntityAlreadyDeactivatedException("Client already deactivated");
        }

        clientExists.setActive(false);
        Client savedClient = repository.save(clientExists);

        log.info("Client with ID: {} successfully deactivated", savedClient.getId());
        return mapper.toDto(savedClient);
    }

    @Transactional
    public ClientResponse activate(UUID id){
        log.info("Activating client with ID: {}", id);
        Client clientExists = findClientByIdOrThrow(id);
        if(clientExists.isActive()){
            log.warn("Client already activated with ID: {}", id);
            throw new EntityAlreadyActivatedException("Client already activated");
        }
        clientExists.setActive(true);
        Client savedClient = repository.save(clientExists);

        log.info("Client with ID: {} successfully activated", savedClient.getId());
        return mapper.toDto(savedClient);
    }
}
