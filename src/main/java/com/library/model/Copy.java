package com.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "copies")
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(targetEntity = Rental.class, mappedBy = "copy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals;

    public enum Status {
        toRent, rented;
    }
}