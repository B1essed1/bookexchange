package com.example.bookexchange.data.model.reference;

import com.example.bookexchange.data.model.system.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "r_reference")
public class Reference extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFERENCE_SEQUENCE")
    @SequenceGenerator(sequenceName = "REFERENCE_SEQUENCE", allocationSize = 1, name = "REFERENCE_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name_uz")
    String nameUz;

    @Column(name = "name_lt")
    String nameLt;

    @Column(name = "name_ru")
    String nameRu;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "code", unique = true)
    String code;

    @Column(name = "value")
    String value = "";

    @Column(name = "sort")
    Integer sort;

    @Column(name = "can_delete")
    Boolean canDelete = false;

    @Transient
    protected String comment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    Reference parent;
}
