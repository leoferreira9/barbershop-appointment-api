package com.leonardo.barbershop.appointment.exception;

public class DateNotValidException extends RuntimeException {
    public DateNotValidException(String message) {
        super(message);
    }
}
