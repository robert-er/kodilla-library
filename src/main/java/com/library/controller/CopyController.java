package com.library.controller;

import com.library.mapper.CopyMapper;
import com.library.model.Copy;
import com.library.model.dto.CopyDto;
import com.library.service.exception.BookNotFoundException;
import com.library.service.exception.CopyNotFoundException;
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

    @PostMapping("add")
    public Long addCopy(@RequestParam Long bookId) throws BookNotFoundException {
        if (bookServiceImplementation.findById(bookId).isPresent()) {
            CopyDto copyDto = new CopyDto(bookId, Copy.Status.toRent);
            return copyServiceImplementation.addNewCopy(copyMapper.mapToCopy(copyDto)).getId();
        } else {
            throw new BookNotFoundException(bookId);
        }
    }

    @GetMapping("get")
    public CopyDto getCopy(@RequestParam Long id) throws CopyNotFoundException {
        return copyMapper.mapToCopyDto(copyServiceImplementation.findById(id)
                .orElseThrow(() -> new CopyNotFoundException(id)));
    }

    @GetMapping("getAll")
    public List<CopyDto> getAll() {
        return copyMapper.mapToCopyDtoList(copyServiceImplementation.getAll());
    }

    @DeleteMapping("delete")
    public void deleteCopy(@RequestParam Long id) throws CopyNotFoundException {
        rentalServiceImplementation.deleteByCopyId(id);
        copyServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public CopyDto updateCopy(@RequestParam Long id,
                              @RequestBody CopyDto copyDto) throws CopyNotFoundException {
        return copyServiceImplementation.findById(id)
                .map(c -> {
                    try {
                        c.setBook(bookServiceImplementation.findById(copyDto.getBookId())
                                .orElseThrow(() -> new BookNotFoundException(copyDto.getBookId())));
                    } catch (BookNotFoundException e) {
                        e.printStackTrace();
                    }
                    c.setStatus(copyDto.getStatus());
                    return copyMapper.mapToCopyDto(copyServiceImplementation.save(c));
                })
                .orElseThrow(() -> new CopyNotFoundException(id));
    }
}
