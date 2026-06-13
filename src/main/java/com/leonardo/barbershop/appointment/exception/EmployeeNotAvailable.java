package com.leonardo.barbershop.appointment.exception;

public class EmployeeNotAvailable extends RuntimeException {
    public EmployeeNotAvailable(String message) {
        super(message);
    }
}
