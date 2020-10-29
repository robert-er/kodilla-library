package com.library.service.exception;

public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException(Long id) {
        super("Rental not found, rental id: " + id);
    }
}
