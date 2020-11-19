package com.library.mapper;

import com.library.model.Rental;
import com.library.dto.RentalDto;
import com.library.service.CopyService;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RentalMapper {

    private final UserService userService;
    private final CopyService copyService;

    public Rental mapToRental(RentalDto rentalDto) {
        Rental rental = new Rental();
        rental.setUser(userService.findById(rentalDto.getUserId()));
        rental.setCopy(copyService.findById(rentalDto.getCopyId()));
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
