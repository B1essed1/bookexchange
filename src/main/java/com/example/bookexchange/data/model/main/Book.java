package com.example.bookexchange.data.model.main;

import com.example.bookexchange.data.model.enums.CoverType;
import com.example.bookexchange.data.model.system.Auditable;
import com.example.bookexchange.data.model.system.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQUENCE")
    @SequenceGenerator(sequenceName = "BOOK_SEQUENCE", allocationSize = 1, name = "BOOK_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotEmpty(message = "kitob nomi kiritilishi shart")
    private String name;
    private String description;
    private int size;
    @NotEmpty
    private String language;

    private String coverType;


    @NotEmpty(message = "kitob soni kiritilishi shart")
    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date publishDate;

    @NotEmpty(message = "Kitob yozuvchisi kiritilishi shart")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "book_authors",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private List<Author> authors = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "book_genres",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_id")})

    private List<Genre> genres = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    private User user;
}
