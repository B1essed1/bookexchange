package com.example.bookexchange.data;

import com.example.bookexchange.data.Enums.CoverType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private Integer size;
    private Enum<CoverType> coverType;
    private Integer quantity;
    private LocalDateTime publishDate;
}
