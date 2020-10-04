package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    Book save(Book book);

    @Override
    void deleteById(Long id);

    @Override
    List<Book> findAll();

    @Override
    Optional<Book> findById(Long id);

    Optional<Book> findByAuthorAndTitleAndYear(String author, String title, int year);

}
