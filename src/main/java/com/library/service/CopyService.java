package com.library.service;

import com.library.model.Copy;
import com.library.service.exception.CopyExistException;
import com.library.service.exception.CopyNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CopyService {

    Copy save(Copy copy);
    Copy addNewCopy(Copy copy) throws CopyExistException;
    void deleteById(Long id) throws CopyNotFoundException;
    List<Copy> getAll();
    Optional<Copy> findById(Long id);

}
