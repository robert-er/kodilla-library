package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto {

    private String name;
    private String surname;
    private LocalDateTime signUpDate;
}
