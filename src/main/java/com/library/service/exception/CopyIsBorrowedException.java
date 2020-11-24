package com.library.service.exception;

public class CopyIsBorrowedException extends RuntimeException {

    public CopyIsBorrowedException(Long id) {
        super("Copy is borrowed, copy id: " + id);
    }
}
