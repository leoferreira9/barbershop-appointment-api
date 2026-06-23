package com.leonardo.barbershop.appointment.exception;

public class ServiceItemNotAvailable extends RuntimeException {
    public ServiceItemNotAvailable(String message) {
        super(message);
    }
}
