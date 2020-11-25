package com.library.controller;

import com.library.mapper.UserMapper;
import com.library.model.User;
import com.library.dto.UserDto;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public Long addUser(@RequestBody UserDto userDto) {
        userDto.setSignUpDate(LocalDateTime.now());
        return userService.addNewUser(userMapper.mapToUser(userDto)).getId();
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userMapper.mapToUserDto(userService.findById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userMapper.mapToUserDtoList(userService.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.findById(id);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setSignUpDate(userDto.getSignUpDate());
        return userMapper.mapToUserDto(userService.save(user));
    }


}
