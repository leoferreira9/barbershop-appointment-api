package com.leonardo.barbershop.appointment.exception;

public class EntityAlreadyCompleted extends RuntimeException {
    public EntityAlreadyCompleted(String message) {
        super(message);
    }
}
