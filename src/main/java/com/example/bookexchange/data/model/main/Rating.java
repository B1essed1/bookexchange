package com.example.bookexchange.data.model.main;

import com.example.bookexchange.data.model.system.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rating
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENRE_SEQUENCE")
    @SequenceGenerator(sequenceName = "GENRE_SEQUENCE", allocationSize = 1, name = "GENRE_SEQUENCE")
    private Long id;
    private int rating;

    @ManyToOne
    @JsonBackReference
    private User user;
}
