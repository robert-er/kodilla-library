package com.library.service;

import com.library.model.Copy;
import java.util.List;

public interface CopyService {

    Copy addNewCopy(Long bookId);
    Copy save(Copy copy);
    void deleteById(Long id);
    List<Copy> getAll();
    Copy findById(Long id);
}
