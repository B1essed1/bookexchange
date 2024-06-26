package com.example.bookexchange.data.model.system;


import com.example.bookexchange.data.model.main.Book;
import com.example.bookexchange.data.model.main.Rating;
import com.example.bookexchange.data.model.reference.Reference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQUENCE")
    @SequenceGenerator(sequenceName = "USER_SEQUENCE", allocationSize = 1, name = "USER_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date expireDate = new Date((long) ((new Date()).getTime() + (1000 * 60 * 2.5)));

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonIgnore
    Reference status;


    @Column(name = "last_use")
    private Date lastUse;

    @Lob
    private byte[] photo;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    Set<Rating> ratings = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    Set<Book> books = new HashSet<>();
}
