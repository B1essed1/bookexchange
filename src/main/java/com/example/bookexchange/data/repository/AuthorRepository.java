package com.example.bookexchange.data.repository;

import com.example.bookexchange.data.model.main.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long>
{
    List<Author>  findAuthorByNamesLikeIgnoreCase(String input);

}
