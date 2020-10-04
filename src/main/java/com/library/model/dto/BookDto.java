package com.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {

    private String author;
    private String title;
    private int year;

}
