package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.appointment.AppointmentRequest;
import com.leonardo.barbershop.appointment.dto.appointment.AppointmentResponse;
import com.leonardo.barbershop.appointment.service.AppointmentService;
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
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointment management", description = "Endpoints for managing appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create appointment", description = "Creates a new appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Client, employee or barber service not found"),
            @ApiResponse(responseCode = "409", description = "Employee or barber service is inactive, or employee is not available on this date")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody @Valid AppointmentRequest request){
        AppointmentResponse savedAppointment = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAppointment.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedAppointment);
    }

    @Operation(summary = "Find appointment by ID", description = "Returns an appointment by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment successfully found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(
            @Parameter(description = "Appointment ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Find all appointments", description = "Returns all appointments")
    @ApiResponse(responseCode = "200", description = "Appointments successfully found")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Cancel appointment", description = "Cancel appointment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment successfully cancelled"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Appointment already cancelled")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(
            @Parameter(description = "Appointment ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.cancel(id));
    }

    @Operation(summary = "Complete appointment", description = "Completes an appointment by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment successfully completed"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Appointment already completed")
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse> complete(
            @Parameter(description = "Appointment ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.complete(id));
    }
}
