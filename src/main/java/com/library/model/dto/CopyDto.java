package com.library.model.dto;

import com.library.model.Book;
import com.library.model.Copy;;
import lombok.Value;

@Value
public class CopyDto {

    private Book book;
    private Copy.Status status;

}
