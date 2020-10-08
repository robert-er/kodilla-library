package com.library.model.dto;

import com.library.model.Copy;;
import lombok.Value;

@Value
public class CopyDto {

    Long bookId;
    Copy.Status status;

}
