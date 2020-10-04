package com.library.mapper;

import com.library.model.Copy;
import com.library.model.dto.CopyDto;

public class CopyMapper {

    public Copy mapToCopy(CopyDto copyDto) {
        Copy copy = new Copy();
        copy.setBook(copyDto.getBook());
        copy.setStatus(copyDto.getStatus());
        return copy;
    }

    public CopyDto mapToCopyDto(Copy copy) {
        return new CopyDto(copy.getBook(), copy.getStatus());
    }

}
