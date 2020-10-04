package com.library.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "copy_id")
    private Copy copy;

    private LocalDateTime dateOfRent;
    private LocalDateTime dateOfReturn;

    public Rental(User user, Copy copy, LocalDateTime dateOfRent, LocalDateTime dateOfReturn) {
        this.user = user;
        this.copy = copy;
        this.dateOfRent = dateOfRent;
        this.dateOfReturn = dateOfReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return user.equals(rental.user) &&
                copy.equals(rental.copy) &&
                dateOfRent.equals(rental.dateOfRent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, copy, dateOfRent);
    }
}
