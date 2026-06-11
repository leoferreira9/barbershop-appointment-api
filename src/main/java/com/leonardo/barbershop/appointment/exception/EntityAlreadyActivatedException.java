package com.leonardo.barbershop.appointment.exception;

public class EntityAlreadyActivatedException extends RuntimeException {
    public EntityAlreadyActivatedException(String message) {
        super(message);
    }
}
