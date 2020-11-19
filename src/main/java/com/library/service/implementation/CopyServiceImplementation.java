package com.library.service.implementation;

import com.library.mapper.CopyMapper;
import com.library.model.Copy;
import com.library.dto.CopyDto;
import com.library.repository.CopyRepository;
import com.library.service.CopyService;
import com.library.service.exception.CopyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CopyServiceImplementation implements CopyService {

    private final CopyRepository copyRepository;
    private final CopyMapper copyMapper;

    @Override
    public Copy addNewCopy(Long bookId) {
        CopyDto copyDto = new CopyDto(bookId, Copy.Status.toRent);
        return save(copyMapper.mapToCopy(copyDto));
    }

    @Override
    public Copy save(Copy copy) {
        return copyRepository.save(copy);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        copyRepository.deleteById(id);
    }

    @Override
    public List<Copy> getAll() {
        return copyRepository.findAll();
    }

    @Override
    public Copy findById(Long id) {
        return copyRepository.findById(id).orElseThrow(() -> new CopyNotFoundException(id));
    }
}
