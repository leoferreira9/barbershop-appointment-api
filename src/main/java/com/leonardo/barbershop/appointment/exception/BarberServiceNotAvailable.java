package com.leonardo.barbershop.appointment.exception;

public class BarberServiceNotAvailable extends RuntimeException {
    public BarberServiceNotAvailable(String message) {
        super(message);
    }
}
