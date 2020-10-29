package com.library.service.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found, book id: " + id);
    }
}
