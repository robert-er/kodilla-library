package com.library.mapper;

import com.library.model.Copy;
import com.library.dto.CopyDto;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CopyMapper {

    private final BookService bookService;

    public Copy mapToCopy(CopyDto copyDto) {
        return Copy.builder()
                .book(bookService.findById(copyDto.getBookId()))
                .status(copyDto.getStatus())
                .build();
    }

    public CopyDto mapToCopyDto(Copy copy) {
        return new CopyDto(copy.getBook().getId(), copy.getStatus());
    }

    public List<CopyDto> mapToCopyDtoList(List<Copy> copyList) {
        return copyList.stream()
                .map(this::mapToCopyDto)
                .collect(Collectors.toList());
    }
}
