package com.library.service.exception;

public class RentalExistException extends RuntimeException {

    public RentalExistException(Long id) {
        super("Rental already exist, rental id: " + id);
    }
}
