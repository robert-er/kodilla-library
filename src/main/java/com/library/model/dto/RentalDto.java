package com.library.model.dto;

import com.library.model.Copy;
import com.library.model.User;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class RentalDto {

    private User user;
    private Copy copy;
    private LocalDateTime dateOfRent;
    private LocalDateTime dateOfReturn;

}
