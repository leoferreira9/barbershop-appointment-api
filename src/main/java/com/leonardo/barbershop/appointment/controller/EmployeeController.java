package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.employee.EmployeePatchRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeRequest;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeResponse;
import com.leonardo.barbershop.appointment.dto.employee.EmployeeUpdateRequest;
import com.leonardo.barbershop.appointment.service.EmployeeService;
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
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee management", description = "Endpoints for managing employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Operation(summary = "Create employee", description = "Creates a new employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody @Valid EmployeeRequest request){
        EmployeeResponse savedEmployee = service.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmployee.id())
                .toUri();

        return ResponseEntity.created(location).body(savedEmployee);
    }

    @Operation(summary = "Find employee by ID", description = "Returns an employee by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee successfully found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(
            @Parameter(description = "Employee ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Find all employees", description = "Returns all employees")
    @ApiResponse(responseCode = "200", description = "Employees successfully found")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            Pageable pageable){
        return ResponseEntity.ok(service.findAll(name, active, pageable));
    }

    @Operation(summary = "Update employee", description = "Updates employee data by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @Parameter(description = "Employee ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id,
            @RequestBody @Valid EmployeeUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Partially update employee", description = "Partially updates an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> partialUpdate(
            @Parameter(description = "Employee ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id,
            @RequestBody @Valid EmployeePatchRequest request){
        return ResponseEntity.ok(service.partialUpdate(id, request));
    }

    @Operation(summary = "Deactivate employee", description = "Deactivates an employee by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee successfully deactivated"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Employee already deactivated")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<EmployeeResponse> deactivate(
            @Parameter(description = "Employee ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.deactivate(id));
    }

    @Operation(summary = "Activate employee", description = "Activates an employee by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee successfully activated"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Employee already activated")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activate(
            @Parameter(description = "Employee ID", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") @PathVariable UUID id){
        return ResponseEntity.ok(service.activate(id));
    }
}
