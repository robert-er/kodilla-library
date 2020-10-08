package com.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private String name;
    private String surname;
    private LocalDateTime signUpDate;

}
