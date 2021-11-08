package com.example.bookexchange.data;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String surname;
    private String password;
    private String username;
    @Lob
    private byte[] photo;

    private String phoneNumber;
    private Integer rating;


}
