package com.library.mapper;

import com.library.model.Copy;
import com.library.model.dto.CopyDto;
import com.library.service.exception.BookNotFoundException;
import com.library.service.implementation.BookServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CopyMapper {

    private final BookServiceImplementation bookServiceImplementation;

    public Copy mapToCopy(CopyDto copyDto) throws BookNotFoundException {
        Copy copy = new Copy();
        copy.setBook(bookServiceImplementation.findById(copyDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(copyDto.getBookId())));
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
