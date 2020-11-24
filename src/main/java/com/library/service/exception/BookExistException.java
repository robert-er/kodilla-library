package com.library.service.exception;

public class BookExistException extends RuntimeException {

    public BookExistException(Long id) {
        super("Book already exist, book id: " + id);
    }
}
