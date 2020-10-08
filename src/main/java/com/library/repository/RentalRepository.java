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
    Rental save(Rental rental);

    @Override
    void deleteById(Long id);

    @Override
    List<Rental> findAll();

    @Override
    Optional<Rental> findById(Long id);

    Optional<Rental> findByUserAndCopyAndDateOfRent(User user, Copy copy, LocalDateTime dateOfRent);

    Optional<Rental> findByUser(User user);

    @Transactional
    void deleteByUser(User user);

    Optional<Rental> findByCopy(Copy copy);

    @Transactional
    void deleteByCopy(Copy copy);

}
