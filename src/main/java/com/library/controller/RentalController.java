package com.library.controller;

import com.library.mapper.RentalMapper;
import com.library.model.Copy;
import com.library.model.User;
import com.library.model.dto.RentalDto;
import com.library.service.exception.CopyIsBorrowedException;
import com.library.service.implementation.CopyServiceImplementation;
import com.library.service.implementation.RentalServiceImplementation;
import com.library.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/rental")
public class RentalController {

    private final RentalServiceImplementation rentalServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;
    private final CopyServiceImplementation copyServiceImplementation;
    private final RentalMapper rentalMapper;

    @PostMapping
    public Long addRental(@RequestParam Long userId,
                          @RequestParam Long copyId)
            throws CopyIsBorrowedException {
        User user = userServiceImplementation.findById(userId);
        Copy copy = copyServiceImplementation.findById(copyId);
        if (copy.getStatus() == Copy.Status.toRent) {
            return rentalServiceImplementation.addNewRental(user, copy).getId();
        } else {
            throw new CopyIsBorrowedException(copyId);
        }
    }

    @GetMapping("{id}")
    public RentalDto getRental(@PathVariable Long id) {
        return rentalMapper.mapToRentalDto(rentalServiceImplementation.findById(id));
    }

    @GetMapping
    public List<RentalDto> getAll() {
        return rentalMapper.mapToRentalDtoList(rentalServiceImplementation.getAll());
    }

    @DeleteMapping("{id}")
    public void deleteRental(@PathVariable Long id) {
        Copy copy = copyServiceImplementation.findById(rentalServiceImplementation.findById(id).getCopy().getId());
        copy.setStatus(Copy.Status.toRent);
        copyServiceImplementation.save(copy);
        rentalServiceImplementation.deleteById(id);
    }

    @PutMapping("{id}")
    public RentalDto updateRental(@PathVariable Long id, @RequestBody RentalDto rentalDto) {
        userServiceImplementation.findById(rentalDto.getUserId());
        copyServiceImplementation.findById(rentalDto.getCopyId());
        deleteRental(id);
        addRental(rentalDto.getUserId(), rentalDto.getCopyId());
        return  rentalDto;
    }
}
