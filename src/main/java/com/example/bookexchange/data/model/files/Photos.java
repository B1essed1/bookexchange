package com.example.bookexchange.data.model.files;

import com.example.bookexchange.data.model.system.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.net.URL;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "photos")
public class Photos extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHOTOS_SEQUENCE")
    @SequenceGenerator(sequenceName = "PHOTOS_SEQUENCE", allocationSize = 1, name = "PHOTOS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private URL imageUrl;
}
