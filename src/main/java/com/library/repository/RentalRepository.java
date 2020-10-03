package com.library.repository;

import com.library.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

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

    boolean exists(Rental rental);
}
