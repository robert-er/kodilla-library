package com.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserDto {

    private String name;
    private String surname;
    private LocalDateTime signUpDate;

}
