package com.library.service;

import com.library.model.Book;
import com.library.service.exception.BookExistException;
import com.library.service.exception.BookNotFoundException;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);
    Book addNewBook(Book book) throws BookExistException;
    void deleteById(Long id) throws BookNotFoundException;
    List<Book> getAll();
    Optional<Book> findById(Long id);

}
