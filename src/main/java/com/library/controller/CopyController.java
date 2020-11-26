package com.library.controller;

import com.library.mapper.CopyMapper;
import com.library.dto.CopyDto;
import com.library.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/copy")
public class CopyController {

    private final CopyService copyService;
    private final CopyMapper copyMapper;

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Long addCopy(@RequestParam Long bookId) {
        return copyService.addNewCopy(bookId).getId();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CopyDto getCopy(@PathVariable Long id) {
        return copyMapper.mapToCopyDto(copyService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<CopyDto> getAll() {
        return copyMapper.mapToCopyDtoList(copyService.getAll());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteCopy(@PathVariable Long id) {
        copyService.deleteById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CopyDto updateCopy(@PathVariable Long id,
                              @RequestBody CopyDto copyDto) {
        CopyDto copyDtoToUpdate = copyMapper.mapToCopyDto(copyService.findById(id));
        copyDtoToUpdate.setBookId(copyDto.getBookId());
        copyDtoToUpdate.setStatus(copyDto.getStatus());
        return copyMapper.mapToCopyDto(copyService.save(copyMapper.mapToCopy(copyDtoToUpdate)));
    }
}
