package com.library.controller;

import com.library.mapper.BookMapper;
import com.library.model.Book;
import com.library.dto.BookDto;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public Long addBook(@RequestBody BookDto bookDto) {
       return bookService.addNewBook(bookMapper.mapToBook(bookDto)).getId();
    }

    @GetMapping("{id}")
    public BookDto getUser(@PathVariable Long id) {
        return bookMapper.mapToBookDto(bookService.findById(id));
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookMapper.mapToBookDtoList(bookService.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Book book = bookService.findById(id);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setYear(bookDto.getYear());
        return bookMapper.mapToBookDto(bookService.save(book));
    }
}
