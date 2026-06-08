package com.leonardo.barbershop.appointment.exception;

public class ServiceAlreadyDeactivatedException extends RuntimeException {
    public ServiceAlreadyDeactivatedException(String message) {
        super(message);
    }
}
