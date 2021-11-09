package com.example.bookexchange.data.model.reference;

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
@Table(name = "r_region")
public class Region extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGION_SEQUENCE")
    @SequenceGenerator(sequenceName = "REGION_SEQUENCE", allocationSize = 1, name = "REGION_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private  String name;

}
