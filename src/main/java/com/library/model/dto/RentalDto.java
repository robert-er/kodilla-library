package com.library.model.dto;

import com.library.model.Copy;
import com.library.model.User;

import java.time.LocalDateTime;

public class RentalDto {

    private User user;
    private Copy copy;
    private LocalDateTime dateOfRent;
    private LocalDateTime dateOfReturn;

}
