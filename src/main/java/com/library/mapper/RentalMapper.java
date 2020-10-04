package com.library.mapper;

import com.library.model.Rental;
import com.library.model.dto.RentalDto;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public Rental mapToRental(RentalDto rentalDto) {
        Rental rental = new Rental();
        rental.setUser(rentalDto.getUser());
        rental.setCopy(rentalDto.getCopy());
        rental.setDateOfRent(rentalDto.getDateOfRent());
        rental.setDateOfReturn(rentalDto.getDateOfReturn());
        return rental;
    }

    public RentalDto mapToRentalDto(Rental rental) {
        return new RentalDto(rental.getUser(), rental.getCopy(), rental.getDateOfRent(), rental.getDateOfReturn());
    }

}
