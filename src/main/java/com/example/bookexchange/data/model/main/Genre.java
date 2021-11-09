package com.example.bookexchange.data.model.main;

import com.example.bookexchange.data.model.system.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_genre")
public class Genre extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENRE_SEQUENCE")
    @SequenceGenerator(sequenceName = "GENRE_SEQUENCE", allocationSize = 1, name = "GENRE_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

}
