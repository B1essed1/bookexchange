package com.example.bookexchange.data.model.main;

import com.example.bookexchange.data.model.system.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_author")
public class Author extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHOR_SEQUENCE")
    @SequenceGenerator(sequenceName = "AUTHOR_SEQUENCE", allocationSize = 1, name = "AUTHOR_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String names;


    @ManyToMany(fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
