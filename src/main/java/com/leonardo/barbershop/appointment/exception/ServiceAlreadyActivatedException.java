package com.leonardo.barbershop.appointment.exception;

public class ServiceAlreadyActivatedException extends RuntimeException {
    public ServiceAlreadyActivatedException(String message) {
        super(message);
    }
}
