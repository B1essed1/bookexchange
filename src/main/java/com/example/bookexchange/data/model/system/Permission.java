package com.example.bookexchange.data.model.system;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_permissions")
public class Permission extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQUENCE")
    @SequenceGenerator(sequenceName = "PERMISSION_SEQUENCE", allocationSize = 1, name = "PERMISSION_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_lt")
    private String nameLt;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
