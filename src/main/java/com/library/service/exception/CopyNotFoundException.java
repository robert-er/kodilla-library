package com.library.service.exception;

public class CopyNotFoundException extends RuntimeException {

    public CopyNotFoundException(Long id) {
        super("Copy not found, copy id: " + id);
    }
}
