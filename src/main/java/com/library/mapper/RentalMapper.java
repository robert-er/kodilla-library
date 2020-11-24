package com.library.mapper;

import com.library.model.Rental;
import com.library.dto.RentalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RentalMapper {

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
