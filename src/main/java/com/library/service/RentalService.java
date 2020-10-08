package com.library.service;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalService {

    Rental save(Rental rental);
    Rental addNewRental(User user, Copy copy) throws RentalExistException;
    Rental returnCopy(Rental rental) throws RentalNotFoundException;
    void extendReturnDate(Rental rental, LocalDateTime returnDate) throws RentalNotFoundException;
    void deleteById(Long id) throws RentalNotFoundException;
    List<Rental> getAll();
    Optional<Rental> findById(Long id);
    Optional<Rental> findByUser(User user);
    void deleteByUser(Optional<User> user);
    Optional<Rental> findByCopy(Copy copy);
    void deleteByCopy(Optional<Copy> copy);

}
