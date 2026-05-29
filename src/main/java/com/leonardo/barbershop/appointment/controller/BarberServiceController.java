package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceRequest;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceResponse;
import com.leonardo.barbershop.appointment.dto.barberservice.BarberServiceUpdateRequest;
import com.leonardo.barbershop.appointment.service.BarberServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/barber-services")
public class BarberServiceController {

    private final BarberServiceService service;

    public BarberServiceController(BarberServiceService service) {
        this.service = service;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<BarberServiceResponse> findById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<BarberServiceResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberServiceResponse> update(@PathVariable UUID id, @RequestBody @Valid BarberServiceUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }
}
