package com.example.bookexchange.data.model.main;


import com.example.bookexchange.data.model.system.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_book_reception_type")
public class BookReceptionType extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_RECEPTION_TYPE_SEQUENCE")
    @SequenceGenerator(sequenceName = "BOOK_RECEPTION_TYPE_SEQUENCE", allocationSize = 1, name = "BOOK_RECEPTION_TYPE_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private  String name;

}
