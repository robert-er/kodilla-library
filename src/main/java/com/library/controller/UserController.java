package com.library.controller;

import com.library.mapper.UserMapper;
import com.library.model.User;
import com.library.model.dto.UserDto;
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

    @PostMapping
    public Long addUser(@RequestBody UserDto userDto) {
        userDto.setSignUpDate(LocalDateTime.now());
        return userServiceImplementation.addNewUser(userMapper.mapToUser(userDto)).getId();
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userMapper.mapToUserDto(userServiceImplementation.findById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userMapper.mapToUserDtoList(userServiceImplementation.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        rentalServiceImplementation.deleteByUserId(id);
        userServiceImplementation.deleteById(id);
    }

    @PutMapping("{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userServiceImplementation.findById(id);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setSignUpDate(userDto.getSignUpDate());
        return userMapper.mapToUserDto(userServiceImplementation.save(user));
    }
}
