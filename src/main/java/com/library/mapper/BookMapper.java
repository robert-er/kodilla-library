package com.library.mapper;

import com.library.model.Book;
import com.library.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book mapToBook(BookDto bookDto) {
        return Book.builder()
                .author(bookDto.getAuthor())
                .title(bookDto.getTitle())
                .year(bookDto.getYear())
                .build();
    }

    public BookDto mapToBookDto(Book book) {
        return new BookDto(book.getAuthor(), book.getTitle(), book.getYear());
    }

    public List<BookDto> mapToBookDtoList(List<Book> bookList) {
        return bookList.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }
}
