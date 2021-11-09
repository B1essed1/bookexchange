package com.example.bookexchange.data.model.main;

import com.example.bookexchange.data.model.enums.CoverType;
import com.example.bookexchange.data.model.system.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_book")
public class Book extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQUENCE")
    @SequenceGenerator(sequenceName = "BOOK_SEQUENCE", allocationSize = 1, name = "BOOK_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;
    private String description;
    private Integer size;
    private Enum<CoverType> coverType;
    private Integer quantity;
    private LocalDateTime publishDate;
}
