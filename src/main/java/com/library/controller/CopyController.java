package com.library.controller;

import com.library.mapper.BookMapper;
import com.library.mapper.CopyMapper;
import com.library.model.Book;
import com.library.model.Copy;
import com.library.model.dto.CopyDto;
import com.library.service.exception.BookNotFoundException;
import com.library.service.exception.CopyNotFoundException;
import com.library.service.implementation.BookServiceImplementation;
import com.library.service.implementation.CopyServiceImplementation;
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
    private final CopyMapper copyMapper;

    @PostMapping("add")
    public void addCopy(@RequestParam Long bookId) throws BookNotFoundException {
        Book book = bookServiceImplementation.findById(bookId).orElseThrow(BookNotFoundException::new);
        CopyDto copyDto = new CopyDto(book, Copy.Status.toRent);
        copyServiceImplementation.addNewCopy(copyMapper.mapToCopy(copyDto));
    }

    @GetMapping("get")
    public CopyDto getCopy(@RequestParam Long id) throws CopyNotFoundException {
        return copyMapper.mapToCopyDto(copyServiceImplementation.findById(id).orElseThrow(CopyNotFoundException::new));
    }

    @GetMapping("getAll")
    public List<CopyDto> getAll() {
        return copyMapper.mapToCopyDtoList(copyServiceImplementation.getAll());
    }

    @DeleteMapping("delete")
    public void deleteCopy(@RequestParam Long id) throws CopyNotFoundException {
        copyServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public CopyDto updateCopy(@RequestParam Long id,
                              @RequestParam Long bookId,
                              @RequestParam Copy.Status status) throws CopyNotFoundException {
        return copyServiceImplementation.findById(id)
                .map(c -> {
                    try {
                        c.setBook(bookServiceImplementation.findById(bookId).orElseThrow(BookNotFoundException::new));
                    } catch (BookNotFoundException e) {
                        e.printStackTrace();
                    }
                    c.setStatus(status);
                    return copyMapper.mapToCopyDto(copyServiceImplementation.save(c));
                })
                .orElseThrow(CopyNotFoundException::new);
    }

}
