package com.library.service.exception;

public class UserExistException extends RuntimeException {

    public UserExistException(Long id) {
        super("User already exist, user id: " + id);
    }
}
