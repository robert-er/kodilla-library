package com.library.mapper;

import com.library.model.Book;
import com.library.model.Copy;
import com.library.model.dto.BookDto;
import com.library.model.dto.CopyDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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

    public List<CopyDto> mapToCopyDtoList(List<Copy> copyList) {
        return copyList.stream()
                .map(this::mapToCopyDto)
                .collect(Collectors.toList());
    }

}
