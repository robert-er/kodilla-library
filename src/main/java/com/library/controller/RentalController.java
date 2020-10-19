package com.library.controller;

import com.library.mapper.CopyMapper;
import com.library.mapper.RentalMapper;
import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.model.dto.RentalDto;
import com.library.service.exception.*;
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

    @PostMapping("add")
    public Long addRental(@RequestParam Long userId,
                          @RequestParam Long copyId)
            throws UserNotFoundException, CopyNotFoundException, RentalExistException, CopyIsBorrowedException {
        User user = userServiceImplementation.findById(userId).orElseThrow(UserNotFoundException::new);
        Copy copy = copyServiceImplementation.findById(copyId).orElseThrow(CopyNotFoundException::new);
        if (copy.getStatus() == Copy.Status.toRent) {
            return rentalServiceImplementation.addNewRental(user, copy).getId();
        } else {
            throw new CopyIsBorrowedException();
        }
    }

    @GetMapping("get")
    public RentalDto getRental(@RequestParam Long id) throws RentalNotFoundException {
        return rentalMapper.mapToRentalDto(rentalServiceImplementation.findById(id).orElseThrow(RentalNotFoundException::new));
    }

    @GetMapping("getAll")
    public List<RentalDto> getAll() {
        return rentalMapper.mapToRentalDtoList(rentalServiceImplementation.getAll());
    }

    @DeleteMapping("delete")
    public void deleteRental(@RequestParam Long id) throws RentalNotFoundException, CopyNotFoundException {
        copyServiceImplementation.findById(rentalServiceImplementation.findById(id)
                .orElseThrow(RentalNotFoundException::new)
                .getCopy()
                .getId())
                .map(cp -> {
                    cp.setStatus(Copy.Status.toRent);
                    return copyServiceImplementation.save(cp);
                        }
                ).orElseThrow(CopyNotFoundException::new);

        rentalServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public RentalDto updateRental(@RequestParam Long id, @RequestBody RentalDto rentalDto)
            throws RentalNotFoundException, UserNotFoundException,
            CopyNotFoundException, RentalExistException, CopyIsBorrowedException {
        if (!userServiceImplementation.findById(rentalDto.getUserId()).isPresent()) {
            throw new UserNotFoundException();
        }
        if (!copyServiceImplementation.findById(rentalDto.getCopyId()).isPresent()) {
            throw new CopyNotFoundException();
        }

        deleteRental(id);
        addRental(rentalDto.getUserId(), rentalDto.getCopyId());
        return  rentalDto;
    }
}
