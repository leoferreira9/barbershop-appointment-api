package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.client.ClientRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.dto.client.ClientUpdateRequest;
import com.leonardo.barbershop.appointment.service.ClientService;
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
@RequestMapping("/api/v1/clients")
@Tag(name = "Client management", description = "Endpoints for managing clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service){
        this.service = service;
    }

    @Operation(summary = "Create client", description = "Creates a new client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid ClientRequest request){
        ClientResponse savedClient = service.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedClient.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedClient);
    }

    @Operation(summary = "Find client by ID", description = "Returns a client by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client successfully found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(
            @Parameter(description = "Client ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Find all clients", description = "Returns all clients")
    @ApiResponse(responseCode = "200", description = "Clients successfully found")
    @GetMapping
    public ResponseEntity<List<ClientResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Update client", description = "Updates client data by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(
            @Parameter(description = "Client ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id,
            @RequestBody @Valid ClientUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Delete client", description = "Delete client by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Client ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
