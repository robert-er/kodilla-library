package com.library.repository;

import com.library.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    void deleteByBookId(Long id);

    List<Copy> findByBookId(Long id);

}
