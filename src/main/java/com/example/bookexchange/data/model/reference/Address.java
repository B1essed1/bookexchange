package com.example.bookexchange.data.model.reference;


import com.example.bookexchange.data.model.system.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "r_address")
public class Address extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQUENCE")
    @SequenceGenerator(sequenceName = "ADDRESS_SEQUENCE", allocationSize = 1, name = "ADDRESS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
}
