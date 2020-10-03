package com.library.repository;

import com.library.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CopyRepository extends JpaRepository<Copy, Long> {

    @Override
    Copy save(Copy copy);

    @Override
    void deleteById(Long id);

    @Override
    List<Copy> findAll();

    @Override
    Optional<Copy> findById(Long id);

}
