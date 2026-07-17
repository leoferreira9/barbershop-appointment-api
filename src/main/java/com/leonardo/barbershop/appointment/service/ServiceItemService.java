package com.leonardo.barbershop.appointment.service;

import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemPatchRequest;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemRequest;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemResponse;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.filters.ServiceItemFilter;
import com.leonardo.barbershop.appointment.mapper.ServiceItemMapper;
import com.leonardo.barbershop.appointment.model.ServiceItem;
import com.leonardo.barbershop.appointment.repository.ServiceItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ServiceItemService {

    private final ServiceItemRepository repository;
    private final ServiceItemMapper mapper;

    public ServiceItemService(ServiceItemRepository repository, ServiceItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private ServiceItem findServiceItemByIdOrThrow(UUID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service item not found with ID: " + id));
    }

    private void updateServiceItemData(ServiceItem serviceItem, ServiceItemUpdateRequest request){
        serviceItem.setName(request.name());
        serviceItem.setPrice(request.price());
        serviceItem.setDescription(request.description());
        serviceItem.setDurationMinutes(request.durationMinutes());
    }

    private void patchServiceItemData(ServiceItem serviceItem, ServiceItemPatchRequest request){
        if(request.name() != null && !request.name().isBlank()){
            serviceItem.setName(request.name());
        }

        if(request.description() != null && !request.description().isBlank()){
            serviceItem.setDescription(request.description());
        }

        if(request.price() != null){
            serviceItem.setPrice(request.price());
        }

        if(request.durationMinutes() != null){
            serviceItem.setDurationMinutes(request.durationMinutes());
        }
    }

    @Transactional
    public ServiceItemResponse create(ServiceItemRequest request){
        log.info("Creating new service item");
        ServiceItem serviceItem = mapper.toEntity(request);
        ServiceItem savedServiceItem = repository.save(serviceItem);
        log.info("Service item created successfully with ID: {}", savedServiceItem.getId());
        return mapper.toDto(savedServiceItem);
    }

    public ServiceItemResponse findById(UUID id){
        log.info("Finding service item with ID: {}", id);
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);
        log.info("Service item with ID: {} successfully found", id);
        return mapper.toDto(serviceItemExists);
    }

    public Page<ServiceItemResponse> findAll(String name, Boolean active, Pageable pageable){
        log.info("Finding service items with filters - name: {}, active: {}", name, active);
        Specification<ServiceItem> specification = Specification.where(ServiceItemFilter.hasName(name))
                .and(ServiceItemFilter.hasActive(active));

        Page<ServiceItemResponse> serviceItemResponses = repository.findAll(specification, pageable).map(mapper::toDto);
        log.info("Found: {} service items", serviceItemResponses.getTotalElements());
        return serviceItemResponses;
    }

    @Transactional
    public ServiceItemResponse update(UUID id, ServiceItemUpdateRequest request){
        log.info("Updating service item with ID: {} (PUT)", id);
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);
        updateServiceItemData(serviceItemExists, request);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        log.info("Service item with ID: {} successfully updated", savedServiceItem.getId());
        return mapper.toDto(savedServiceItem);
    }

    @Transactional
    public ServiceItemResponse partialUpdate(UUID id, ServiceItemPatchRequest request){
        log.info("Updating service item with ID: {} (PATCH)", id);
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);
        patchServiceItemData(serviceItemExists, request);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        log.info("Service item with ID: {} partially updated", savedServiceItem.getId());
        return mapper.toDto(savedServiceItem);
    }

    @Transactional
    public ServiceItemResponse deactivate(UUID id){
        log.info("Deactivating service item with ID: {}", id);
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);

        if(!serviceItemExists.isActive()){
            log.warn("Service item already deactivated with ID: {}", id);
            throw new EntityAlreadyDeactivatedException("Service item already deactivated");
        }

        serviceItemExists.setActive(false);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        log.info("Service item with ID: {} successfully deactivated", savedServiceItem.getId());
        return mapper.toDto(savedServiceItem);
    }

    @Transactional
    public ServiceItemResponse activate(UUID id){
        log.info("Activating service item with ID: {}", id);
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);

        if(serviceItemExists.isActive()){
            log.warn("Service item already activated with ID: {}", id);
            throw new EntityAlreadyActivatedException("Service item already activated");
        }

        serviceItemExists.setActive(true);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        log.info("Service item with ID: {} successfully activated", savedServiceItem.getId());
        return mapper.toDto(savedServiceItem);
    }
}
