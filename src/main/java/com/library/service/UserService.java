package com.library.service;

import com.library.model.User;
import com.library.service.exception.UserExistException;
import com.library.service.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);
    User addNewUser(User user) throws UserExistException;
    void deleteById(Long id) throws UserNotFoundException;
    List<User> getAll();
    Optional<User> findById(Long id) throws UserNotFoundException;

}
