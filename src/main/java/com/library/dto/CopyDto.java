package com.library.dto;

import com.library.model.Copy;;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CopyDto {

    Long bookId;
    Copy.Status status;
}
