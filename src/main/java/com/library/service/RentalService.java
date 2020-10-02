package com.library.service;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface RentalService {

    Rental save(Rental rental);
    Rental addNewRental(User user, Copy copy) throws RentalExistException;
    Rental returnCopy(Rental rental) throws RentalNotFoundException;
    void extendReturnDate(Long id, LocalDateTime returnDate) throws RentalNotFoundException;
    void deleteById(Long id) throws RentalNotFoundException;
    Collection<Rental> getAll();
    Optional<Rental> findById(Long id);

}
