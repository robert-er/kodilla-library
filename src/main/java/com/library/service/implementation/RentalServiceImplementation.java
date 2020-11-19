package com.library.service.implementation;

import com.library.model.Copy;
import com.library.model.Rental;
import com.library.model.User;
import com.library.repository.CopyRepository;
import com.library.repository.RentalRepository;
import com.library.repository.UserRepository;
import com.library.service.RentalService;
import com.library.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RentalServiceImplementation implements RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final CopyRepository copyRepository;

    private final static int STANDARD_RENTAL_TIME_IN_DAYS = 14;

    @Override
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental addNewRental(Long userId, Long copyId) {
        User user = validateUser(userId);
        Copy copy = validateCopy(copyId);
        validateCopyStatus(copy);
        validateRental(user, copy);
        Rental rental = new Rental(user, copy, LocalDateTime.now(),
                LocalDateTime.now().plusDays(STANDARD_RENTAL_TIME_IN_DAYS));
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
    public Rental findById(Long id) throws RentalNotFoundException {
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

    private User validateUser(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Copy validateCopy(Long copyId) throws CopyNotFoundException {
        Optional<Copy> copy = copyRepository.findById(copyId);
        return copy.orElseThrow(() -> new CopyNotFoundException(copyId));
    }

    private void validateCopyStatus(Copy copy) throws CopyIsBorrowedException {
        if (copy.getStatus() == Copy.Status.rented) {
            throw new CopyIsBorrowedException(copy.getId());
        }
    }

    private void validateRental(User user, Copy copy) throws RentalExistException {
        Optional<Rental> rental = rentalRepository.findByUserAndCopy(user, copy);
        if(rental.isPresent()) {
            throw new RentalExistException(rental.get().getId());
        }
    }
}
