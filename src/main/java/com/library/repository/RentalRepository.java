package com.library.repository;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long>  {

    @Override
    <S extends Rental> S save(S rental);

    @Override
    void deleteById(Long id);

    @Override
    List<Rental> findAll();

    @Override
    Optional<Rental> findById(Long id);

    Optional<Rental> findByUserAndCopyAndDateOfRent(User user, Copy copy, LocalDateTime dateOfRent);

    Optional<Rental> findByUserAndCopy(User user, Copy copy);

    List<Rental> findByUserId(Long id);

    @Transactional
    void deleteByUserId(Long id);

    @Transactional
    void deleteByCopyId(Long id);
}
