package com.library.controller;

import com.library.mapper.BookMapper;
import com.library.model.Book;
import com.library.model.dto.BookDto;
import com.library.service.implementation.BookServiceImplementation;
import com.library.service.implementation.CopyServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/book")
public class BookController {

    private final BookServiceImplementation bookServiceImplementation;
    private final CopyServiceImplementation copyServiceImplementation;
    private final BookMapper bookMapper;

    @PostMapping
    public Long addBook(@RequestBody BookDto bookDto) {
       return bookServiceImplementation.addNewBook(bookMapper.mapToBook(bookDto)).getId();
    }

    @GetMapping("{id}")
    public BookDto getUser(@PathVariable Long id) {
        return bookMapper.mapToBookDto(bookServiceImplementation.findById(id));
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookMapper.mapToBookDtoList(bookServiceImplementation.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        copyServiceImplementation.deleteByBookId(id);
        bookServiceImplementation.deleteById(id);
    }

    @PutMapping("{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Book book = bookServiceImplementation.findById(id);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setYear(bookDto.getYear());
        return bookMapper.mapToBookDto(bookServiceImplementation.save(book));
    }
}
