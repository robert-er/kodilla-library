package com.library.controller;

import com.library.mapper.RentalMapper;
import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.dto.RentalDto;
import com.library.service.CopyService;
import com.library.service.RentalService;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library/rental")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final CopyService copyService;
    private final RentalMapper rentalMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Long addRental(@RequestParam Long userId, @RequestParam Long copyId) {
        return rentalService.addNewRental(userId, copyId).getId();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public RentalDto getRental(@PathVariable Long id) {
        return rentalMapper.mapToRentalDto(rentalService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<RentalDto> getAll() {
        return rentalMapper.mapToRentalDtoList(rentalService.getAll());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteRental(@PathVariable Long id) {
        Copy copy = copyService.findById(rentalService.findById(id).getCopy().getId());
        copy.setStatus(Copy.Status.toRent);
        copyService.save(copy);
        rentalService.deleteById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public RentalDto updateRental(@PathVariable Long id, @RequestBody RentalDto rentalDto) {
        User user = userService.findById(rentalDto.getUserId());
        Copy copy = copyService.findById(rentalDto.getCopyId());
        Rental rental = rentalService.findById(id);
        rental.setUser(user);
        rental.setCopy(copy);
        return rentalMapper.mapToRentalDto(rentalService.save(rental));
    }
}
