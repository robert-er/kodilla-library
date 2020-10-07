package com.library.mapper;

import com.library.model.Book;
import com.library.model.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book mapToBook(BookDto bookDto) {
        Book book = new Book();
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setYear(bookDto.getYear());
        return book;
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
