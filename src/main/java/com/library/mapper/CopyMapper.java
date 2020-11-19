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
        Copy copy = new Copy();
        copy.setBook(bookService.findById(copyDto.getBookId()));
        copy.setStatus(copyDto.getStatus());
        return copy;
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
