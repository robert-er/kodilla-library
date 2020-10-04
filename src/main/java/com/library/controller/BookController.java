package com.library.controller;

import com.library.mapper.BookMapper;
import com.library.model.dto.BookDto;
import com.library.service.exception.BookExistException;
import com.library.service.exception.BookNotFoundException;
import com.library.service.implementation.BookServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/book")
public class BookController {

    private final BookServiceImplementation bookServiceImplementation;
    private final BookMapper bookMapper;

    @PostMapping("add")
    public void addBook(@RequestBody BookDto bookDto) throws BookExistException {
       bookServiceImplementation.addNewBook(bookMapper.mapToBook(bookDto));
    }

    @GetMapping("get")
    public BookDto getUser(@RequestParam Long id) throws BookNotFoundException {
        return bookMapper.mapToBookDto(bookServiceImplementation.findById(id).orElseThrow(BookNotFoundException::new));
    }

    @GetMapping("getAll")
    public List<BookDto> getAll() {
        return bookMapper.mapToBookDtoList(bookServiceImplementation.getAll());
    }

    @DeleteMapping("delete")
    public void deleteBook(@RequestParam Long id) throws BookNotFoundException {
        bookServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public BookDto updateBook(@RequestParam Long id, @RequestBody BookDto bookDto) throws BookNotFoundException {
        return bookServiceImplementation.findById(id)
                .map(b -> {
                    b.setAuthor(bookDto.getAuthor());
                    b.setTitle(bookDto.getTitle());
                    b.setYear(bookDto.getYear());
                    return bookMapper.mapToBookDto(bookServiceImplementation.save(b));
                })
                .orElseThrow(BookNotFoundException::new);
    }
}
