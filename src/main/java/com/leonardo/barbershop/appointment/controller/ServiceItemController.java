package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemRequest;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemResponse;
import com.leonardo.barbershop.appointment.dto.serviceItem.ServiceItemUpdateRequest;
import com.leonardo.barbershop.appointment.service.ServiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-items")
@Tag(name = "Service item management", description = "Endpoints for managing service items")
public class ServiceItemController {

    private final ServiceItemService service;

    public ServiceItemController(ServiceItemService service) {
        this.service = service;
    }

    @Operation(summary = "Create service item", description = "Creates a new service item")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<ServiceItemResponse> create(@RequestBody @Valid ServiceItemRequest request){
        ServiceItemResponse savedServiceItem = service.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedServiceItem.id())
                .toUri();

        return ResponseEntity.created(location).body(savedServiceItem);
    }

    @Operation(summary = "Find service item by ID", description = "Returns a service item by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service item successfully found"),
            @ApiResponse(responseCode = "404", description = "Service item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceItemResponse> findById(
            @Parameter(description = "Service item ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Find all service items", description = "Returns all service items")
    @ApiResponse(responseCode = "200", description = "Service items successfully found")
    @GetMapping
    public ResponseEntity<Page<ServiceItemResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            Pageable pageable){
        return ResponseEntity.ok(service.findAll(name, active, pageable));
    }

    @Operation(summary = "Update service item", description = "Update service item data by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service item successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Service item not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceItemResponse> update(
            @Parameter(description = "Service item ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id,
            @RequestBody @Valid ServiceItemUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deactivate service item", description = "Deactivates a service item by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service item successfully deactivated"),
            @ApiResponse(responseCode = "404", description = "Service item not found"),
            @ApiResponse(responseCode = "409", description = "Service item already deactivated")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ServiceItemResponse> deactivate(@PathVariable UUID id){
        return ResponseEntity.ok(service.deactivate(id));
    }

    @Operation(summary = "Activate service item", description = "Activates a service item by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service item successfully activated"),
            @ApiResponse(responseCode = "404", description = "Service item not found"),
            @ApiResponse(responseCode = "409", description = "Service item already activated")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ServiceItemResponse> activate(@PathVariable UUID id){
        return ResponseEntity.ok(service.activate(id));
    }
}
