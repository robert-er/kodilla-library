package com.library.service;

import com.library.model.Rental;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalService {

    Rental save(Rental rental);
    Rental addNewRental(Long userId, Long copyId) throws RentalExistException;
    Rental returnCopy(Rental rental) throws RentalNotFoundException;
    void extendReturnDate(Rental rental, LocalDateTime returnDate) throws RentalNotFoundException;
    void deleteById(Long id) throws RentalNotFoundException;
    List<Rental> getAll();
    Rental findById(Long id);
    void deleteByUserId(Long userId);
}
