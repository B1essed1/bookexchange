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
@Table(name = "r_district")
public class District extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISTRICT_SEQUENCE")
    @SequenceGenerator(sequenceName = "DISTRICT_SEQUENCE", allocationSize = 1, name = "DISTRICT_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;
}
