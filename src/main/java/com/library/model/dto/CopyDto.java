package com.library.model.dto;

import com.library.model.Book;
import com.library.model.Copy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CopyDto {

    private Book book;
    private Copy.Status status;

}
