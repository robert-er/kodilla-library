package com.library.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RentalDto {

    Long userId;
    Long copyId;
    LocalDateTime dateOfRent;
    LocalDateTime dateOfReturn;
}
