package com.library.mapper;

import com.library.model.Book;
import com.library.model.dto.BookDto;

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

}
