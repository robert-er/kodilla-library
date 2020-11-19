package com.library.service.implementation;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.service.RentalService;
import com.library.service.UserService;
import com.library.service.exception.UserExistException;
import com.library.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RentalService rentalService;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User addNewUser(User user) throws UserExistException {
        if (userRepository.findByNameAndSurname(user.getName(), user.getSurname()).isPresent()) {
            throw new UserExistException(userRepository
                    .findByNameAndSurname(user.getName(), user.getSurname()).get().getId());
        }
        return save(user);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        rentalService.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
            return userRepository.findAll();
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
