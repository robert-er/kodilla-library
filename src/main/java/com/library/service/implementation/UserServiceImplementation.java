package com.library.service.implementation;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import com.library.service.exception.UserExistException;
import com.library.service.exception.UserNotFoundException;

import java.util.Collection;
import java.util.Optional;

public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addNewUser(User user) throws UserExistException {
        if (userRepository.exists(user)) {
            throw new UserExistException();
        }
        return save(user);
    }

    @Override
    public void deleteById(Long id) throws UserNotFoundException {
        if (findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
