package com.library.service.exception;

public class CopyExistException extends RuntimeException {

    public CopyExistException(Long id) {
        super("Copy already exist, copy id: " + id);
    }
}
