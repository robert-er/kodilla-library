package com.library.mapper;

import com.library.model.Rental;
import com.library.model.dto.RentalDto;
import com.library.service.exception.CopyNotFoundException;
import com.library.service.exception.UserNotFoundException;
import com.library.service.implementation.CopyServiceImplementation;
import com.library.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RentalMapper {

    private final UserServiceImplementation userServiceImplementation;
    private final CopyServiceImplementation copyServiceImplementation;

    public Rental mapToRental(RentalDto rentalDto) throws UserNotFoundException, CopyNotFoundException {
        Rental rental = new Rental();
        rental.setUser(userServiceImplementation.findById(rentalDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(rentalDto.getUserId())));
        rental.setCopy(copyServiceImplementation.findById(rentalDto.getCopyId())
                .orElseThrow(() -> new CopyNotFoundException(rentalDto.getCopyId())));
        rental.setDateOfRent(rentalDto.getDateOfRent());
        rental.setDateOfReturn(rentalDto.getDateOfReturn());
        return rental;
    }

    public RentalDto mapToRentalDto(Rental rental) {
        return new RentalDto(rental.getUser().getId(), rental.getCopy().getId(),
                rental.getDateOfRent(), rental.getDateOfReturn());
    }

    public List<RentalDto> mapToRentalDtoList(List<Rental> rentalList) {
        return rentalList.stream()
                .map(this::mapToRentalDto)
                .collect(Collectors.toList());
    }
}
