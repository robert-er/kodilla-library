package com.library.service.implementation;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.service.exception.BookExistException;
import com.library.service.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImplementation implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book addNewBook(Book book) throws BookExistException {
        if (bookRepository.findByAuthorAndTitleAndYear(book.getAuthor(), book.getTitle(), book.getYear()).isPresent()) {
            throw new BookExistException(bookRepository
                    .findByAuthorAndTitleAndYear(book.getAuthor(), book.getTitle(), book.getYear()).get().getId());
        }
        return save(book);
    }

    @Override
    public void deleteById(Long id) throws BookNotFoundException {
        if (findById(id).isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
}
