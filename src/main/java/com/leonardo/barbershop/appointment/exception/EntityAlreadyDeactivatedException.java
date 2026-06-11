package com.leonardo.barbershop.appointment.exception;

public class EntityAlreadyDeactivatedException extends RuntimeException {
    public EntityAlreadyDeactivatedException(String message) {
        super(message);
    }
}
