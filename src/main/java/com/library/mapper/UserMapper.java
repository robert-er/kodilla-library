package com.library.mapper;

import com.library.model.User;
import com.library.model.dto.UserDto;

public class UserMapper {

    public User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setSignUpDate(userDto.getSignUpDate());
        return user;
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getName(), user.getSurname(), user.getSignUpDate());
    }

}
