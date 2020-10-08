package com.library.controller;

import com.library.mapper.UserMapper;
import com.library.model.dto.UserDto;
import com.library.service.exception.UserExistException;
import com.library.service.exception.UserNotFoundException;
import com.library.service.implementation.RentalServiceImplementation;
import com.library.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/user")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;
    private final RentalServiceImplementation rentalServiceImplementation;
    private final UserMapper userMapper;

    @PostMapping("add")
    public void addUser(@RequestBody UserDto userDto) throws UserExistException {
        userDto.setSignUpDate(LocalDateTime.now());
        userServiceImplementation.addNewUser(userMapper.mapToUser(userDto));
    }

    @GetMapping("get")
    public UserDto getUser(@RequestParam Long id) throws UserNotFoundException {
        return userMapper.mapToUserDto(userServiceImplementation.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @GetMapping("getAll")
    public List<UserDto> getAll() {
        return userMapper.mapToUserDtoList(userServiceImplementation.getAll());
    }

    @DeleteMapping("delete")
    public void deleteUser(@RequestParam Long id) throws UserNotFoundException {
        rentalServiceImplementation.deleteByUser(userServiceImplementation.findById(id));
        userServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public UserDto updateUser(@RequestParam Long id, @RequestBody UserDto userDto) throws UserNotFoundException {
        return userServiceImplementation.findById(id)
                .map(u -> {
                    u.setName(userDto.getName());
                    u.setSurname(userDto.getSurname());
                    u.setSignUpDate(userDto.getSignUpDate());
                    return userMapper.mapToUserDto(userServiceImplementation.save(u));
                })
                .orElseThrow(UserNotFoundException::new);
    }
}
