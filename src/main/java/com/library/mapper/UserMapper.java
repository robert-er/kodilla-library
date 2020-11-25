package com.library.mapper;

import com.library.model.User;
import com.library.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getName(), user.getSurname(), user.getSignUpDate());
    }

    public List<UserDto> mapToUserDtoList(List<User> userList) {
            return userList.stream()
                    .map(this::mapToUserDto)
                    .collect(Collectors.toList());
    }
}
