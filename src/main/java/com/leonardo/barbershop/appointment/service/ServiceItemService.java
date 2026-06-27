package com.leonardo.barbershop.appointment.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Transactional
    public ServiceItemResponse create(ServiceItemRequest request){
        ServiceItem serviceItem = mapper.toEntity(request);
        ServiceItem savedServiceItem = repository.save(serviceItem);
        return mapper.toDto(savedServiceItem);
    }

    public ServiceItemResponse findById(UUID id){
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);
        return mapper.toDto(serviceItemExists);
    }

    public Page<ServiceItemResponse> findAll(String name, Boolean active, Pageable pageable){

        Specification<ServiceItem> specification = Specification.where(ServiceItemFilter.hasName(name))
                .and(ServiceItemFilter.hasActive(active));

        return repository.findAll(specification, pageable).map(mapper::toDto);
    }

    @Transactional
    public ServiceItemResponse update(UUID id, ServiceItemUpdateRequest request){
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);
        updateServiceItemData(serviceItemExists, request);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        return mapper.toDto(savedServiceItem);
    }

    @Transactional
    public ServiceItemResponse deactivate(UUID id){
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);

        if(!serviceItemExists.isActive())
            throw new EntityAlreadyDeactivatedException("Service item already deactivated");

        serviceItemExists.setActive(false);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        return mapper.toDto(savedServiceItem);
    }

    @Transactional
    public ServiceItemResponse activate(UUID id){
        ServiceItem serviceItemExists = findServiceItemByIdOrThrow(id);

        if(serviceItemExists.isActive())
            throw new EntityAlreadyActivatedException("Service item already activated");

        serviceItemExists.setActive(true);
        ServiceItem savedServiceItem = repository.save(serviceItemExists);
        return mapper.toDto(savedServiceItem);
    }
}
