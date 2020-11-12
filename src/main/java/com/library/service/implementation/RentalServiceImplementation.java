package com.library.service.implementation;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.repository.RentalRepository;
import com.library.service.RentalService;
import com.library.service.exception.RentalExistException;
import com.library.service.exception.RentalNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
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
        Rental rental = new Rental(user, copy, LocalDateTime.now(),
                LocalDateTime.now().plusDays(STANDARD_RENTAL_TIME_IN_DAYS));
        if (rentalRepository
                .findByUserAndCopyAndDateOfRent(
                        rental.getUser(),
                        rental.getCopy(),
                        rental.getDateOfRent()
                ).isPresent()) {
            throw new RentalExistException(rentalRepository
                    .findByUserAndCopyAndDateOfRent(
                            rental.getUser(),
                            rental.getCopy(),
                            rental.getDateOfRent()
                    ).get().getId());
        }
        copy.setStatus(Copy.Status.rented);
        return save(rental);
    }

    @Override
    public Rental returnCopy(Rental rental) throws RentalNotFoundException {
        if (rentalRepository
                .findByUserAndCopyAndDateOfRent(
                        rental.getUser(),
                        rental.getCopy(),
                        rental.getDateOfRent()
                ).isPresent()) {
            rental.getCopy().setStatus(Copy.Status.toRent);
            return rental;
        } else {
            throw new RentalNotFoundException(rental.getId());
        }
    }

    @Override
    public void extendReturnDate(Rental rental, LocalDateTime returnDate) throws RentalNotFoundException {
        if (rentalRepository
                .findByUserAndCopyAndDateOfRent(
                        rental.getUser(),
                        rental.getCopy(),
                        rental.getDateOfRent()
                ).isPresent()) {
            rental.setDateOfReturn(returnDate);
        } else {
            throw new RentalNotFoundException(rental.getId());
        }
    }

    @Override
    public void deleteById(Long id) throws RentalNotFoundException {
        findById(id);
        rentalRepository.deleteById(id);
    }

    @Override
    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @Override
    public Rental findById(Long id) {
        return rentalRepository.findById(id).orElseThrow(() -> new RentalNotFoundException(id));
    }

    @Override
    public void deleteByUserId(Long userId) {
        Optional.ofNullable(userId).ifPresent(id -> {
            rentalRepository.findByUserId(id).forEach(rental -> rental.getCopy().setStatus(Copy.Status.toRent));
            rentalRepository.deleteByUserId(id);
                }
        );
    }

    @Override
    public void deleteByCopyId(Long copyId) {
        Optional.ofNullable(copyId).ifPresent(rentalRepository::deleteByCopyId);
    }
}
