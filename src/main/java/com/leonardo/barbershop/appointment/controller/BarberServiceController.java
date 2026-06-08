package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceRequest;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceResponse;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceUpdateRequest;
import com.leonardo.barbershop.appointment.service.BarberServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/barber-services")
@Tag(name = "Barber service management", description = "Endpoints for managing barber services")
public class BarberServiceController {

    private final BarberServiceService service;

    public BarberServiceController(BarberServiceService service) {
        this.service = service;
    }

    @Operation(summary = "Create barber service", description = "Creates a new barber service")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Barber service created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<BarberServiceResponse> create(@RequestBody @Valid BarberServiceRequest request){
        BarberServiceResponse savedBarberService = service.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBarberService.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedBarberService);
    }

    @Operation(summary = "Find barber service by ID", description = "Returns a barber service by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barber service successfully found"),
            @ApiResponse(responseCode = "404", description = "Barber service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BarberServiceResponse> findById(
            @Parameter(description = "Barber service ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Find all barber services", description = "Returns all barber services")
    @ApiResponse(responseCode = "200", description = "Barber services successfully found")
    @GetMapping
    public ResponseEntity<List<BarberServiceResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Update barber service", description = "Updates barber service data by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barber service successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Barber service not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BarberServiceResponse> update(
            @Parameter(description = "Barber service ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id,
            @RequestBody @Valid BarberServiceUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deactivate barber service", description = "Deactivates a barber service by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barber service successfully deactivated"),
            @ApiResponse(responseCode = "404", description = "Barber service not found"),
            @ApiResponse(responseCode = "409", description = "Barber service already deactivated")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<BarberServiceResponse> deactivate(@PathVariable UUID id){
        return ResponseEntity.ok(service.deactivate(id));
    }

    @Operation(summary = "Activate barber service", description = "Activates a barber service by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barber service successfully activated"),
            @ApiResponse(responseCode = "404", description = "Barber service not found"),
            @ApiResponse(responseCode = "409", description = "Barber service already activated")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<BarberServiceResponse> activate(@PathVariable UUID id){
        return ResponseEntity.ok(service.activate(id));
    }
}
