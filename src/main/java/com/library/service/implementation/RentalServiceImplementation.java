package com.library.service.implementation;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.repository.RentalRepository;
import com.library.service.RentalService;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public class RentalServiceImplementation implements RentalService {

    private final RentalRepository rentalRepository;

    private final static int STANDARD_RENTAL_TIME_IN_DAYS = 14;

    public RentalServiceImplementation(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental addNewRental(User user, Copy copy) throws RentalExistException {
        Rental newRental = new Rental(user, copy, LocalDateTime.now(),
                LocalDateTime.now().plusDays(STANDARD_RENTAL_TIME_IN_DAYS));
        if (rentalRepository.exists(newRental)) {
            throw new RentalExistException();
        }
        copy.setStatus(Copy.Status.TAKEN);
        return save(newRental);
    }

    @Override
    public Rental returnCopy(Rental rental) throws RentalNotFoundException {
        if (rentalRepository.exists(rental)) {
            rental.getCopy().setStatus(Copy.Status.TO_RENT);
            return rental;
        } else {
            throw new RentalNotFoundException();
        }
    }

    @Override
    public void extendReturnDate(Rental rental, LocalDateTime returnDate) throws RentalNotFoundException {
        if (rentalRepository.exists(rental)) {
            rental.setDateOfReturn(returnDate);
        } else {
            throw new RentalNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) throws RentalNotFoundException {
        if (findById(id).isPresent()) {
            rentalRepository.deleteById(id);
        } else {
            throw new RentalNotFoundException();
        }
    }

    @Override
    public Collection<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @Override
    public Optional<Rental> findById(Long id) {
        return rentalRepository.findById(id);
    }
}
