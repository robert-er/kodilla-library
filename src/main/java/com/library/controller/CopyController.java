package com.library.controller;

import com.library.mapper.CopyMapper;
import com.library.model.Copy;
import com.library.model.dto.CopyDto;
import com.library.service.implementation.BookServiceImplementation;
import com.library.service.implementation.CopyServiceImplementation;
import com.library.service.implementation.RentalServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/copy")
public class CopyController {

    private final CopyServiceImplementation copyServiceImplementation;
    private final BookServiceImplementation bookServiceImplementation;
    private final RentalServiceImplementation rentalServiceImplementation;
    private final CopyMapper copyMapper;

    @PostMapping
    public Long addCopy(@RequestParam Long bookId) {
        bookServiceImplementation.findById(bookId);
        CopyDto copyDto = new CopyDto(bookId, Copy.Status.toRent);
        return copyServiceImplementation.addNewCopy(copyMapper.mapToCopy(copyDto)).getId();
    }

    @GetMapping("{id}")
    public CopyDto getCopy(@PathVariable Long id) {
        return copyMapper.mapToCopyDto(copyServiceImplementation.findById(id));
    }

    @GetMapping
    public List<CopyDto> getAll() {
        return copyMapper.mapToCopyDtoList(copyServiceImplementation.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteCopy(@PathVariable Long id) {
        rentalServiceImplementation.deleteByCopyId(id);
        copyServiceImplementation.deleteById(id);
    }

    @PutMapping("{id}")
    public CopyDto updateCopy(@PathVariable Long id,
                              @RequestBody CopyDto copyDto) {
        CopyDto copyDtoToUpdate = copyMapper.mapToCopyDto(copyServiceImplementation.findById(id));
        copyDtoToUpdate.setBookId(copyDto.getBookId());
        copyDtoToUpdate.setStatus(copyDto.getStatus());
        return copyMapper.mapToCopyDto(copyServiceImplementation.save(copyMapper.mapToCopy(copyDtoToUpdate)));
    }
}
