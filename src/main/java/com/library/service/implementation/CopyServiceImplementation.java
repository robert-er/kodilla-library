package com.library.service.implementation;

import com.library.model.Copy;
import com.library.repository.CopyRepository;
import com.library.service.CopyService;
import com.library.service.exception.CopyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CopyServiceImplementation implements CopyService {

    private final CopyRepository copyRepository;

    public CopyServiceImplementation(CopyRepository copyRepository) {
        this.copyRepository = copyRepository;
    }

    @Override
    public Copy save(Copy copy) {
        return copyRepository.save(copy);
    }

    @Override
    public Copy addNewCopy(Copy copy) {
        copy.setStatus(Copy.Status.toRent);
        return save(copy);
    }

    @Override
    public void deleteById(Long id) throws CopyNotFoundException {
        if (findById(id).isPresent()) {
            copyRepository.deleteById(id);
        } else {
            throw new CopyNotFoundException();
        }
    }

    @Override
    public List<Copy> getAll() {
        return copyRepository.findAll();
    }

    @Override
    public Optional<Copy> findById(Long id) {
        return copyRepository.findById(id);
    }
}
