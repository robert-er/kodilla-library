package com.library.model.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UserDto {

    private String name;
    private String surname;
    private LocalDateTime signUpDate;

}
