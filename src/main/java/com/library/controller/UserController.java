package com.library.controller;

import com.library.mapper.UserMapper;
import com.library.model.dto.UserDto;
import com.library.service.exception.UserExistException;
import com.library.service.exception.UserNotFoundException;
import com.library.service.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/library/user")
public class UserController {

    private UserServiceImplementation userServiceImplementation;
    private UserMapper userMapper;


    public UserServiceImplementation getUserServiceImplementation() {
        return userServiceImplementation;
    }

    public UserMapper getUserMapper() {
        return userMapper;
    }

    @Autowired
    public void setUserServiceImplementation(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("add")
    public void addUser(@RequestBody UserDto userDto) throws UserExistException {
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
