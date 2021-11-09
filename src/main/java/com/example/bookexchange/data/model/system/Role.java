package com.example.bookexchange.data.model.system;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple domain object that represents application user's role - ADMIN, USER, etc.
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_roles")
public class Role extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQUENCE")
    @SequenceGenerator(sequenceName = "ROLE_SEQUENCE", allocationSize = 1, name = "ROLE_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_permissions",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions = new LinkedList<>();

    @Override
    public String toString() {
        return "Role{" +
                "id: " + id + ", " +
                "name: " + name + ", " +
                "code: " + code + "}";
    }
}
