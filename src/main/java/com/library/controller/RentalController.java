package com.library.controller;

import com.library.mapper.RentalMapper;
import com.library.model.Copy;
import com.library.model.User;
import com.library.model.dto.RentalDto;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;
import com.library.service.exception.UserNotFoundException;
import com.library.service.exception.CopyNotFoundException;
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
    public void addRental(@RequestParam Long userId,
                          @RequestParam Long copyId)
            throws UserNotFoundException, CopyNotFoundException, RentalExistException {
        User user = userServiceImplementation.findById(userId).orElseThrow(UserNotFoundException::new);
        Copy copy = copyServiceImplementation.findById(copyId).orElseThrow(CopyNotFoundException::new);
        if (copy.getStatus() == Copy.Status.toRent) {
            rentalServiceImplementation.addNewRental(user, copy);
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
    public void deleteRental(@RequestParam Long id) throws RentalNotFoundException {
        rentalServiceImplementation.deleteById(id);
    }

    @PutMapping("update")
    public RentalDto updateRental(@RequestParam Long id, @RequestBody RentalDto rentalDto) throws RentalNotFoundException {
        return rentalServiceImplementation.findById(id)
                .map(r -> {
                    r.setUser(rentalDto.getUser());
                    r.setCopy(rentalDto.getCopy());
                    r.setDateOfRent(rentalDto.getDateOfRent());
                    r.setDateOfReturn(rentalDto.getDateOfReturn());
                    return rentalMapper.mapToRentalDto(rentalServiceImplementation.save(r));
                })
                .orElseThrow(RentalNotFoundException::new);
    }
}
