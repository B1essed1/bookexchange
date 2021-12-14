package com.example.bookexchange.data.repository;

import com.example.bookexchange.data.model.main.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>
{

    List<Book> findBookByNameLike(String input);
    List<Book> findByNameContaining(String string);

    List<Book> findByNameLike(String input);
    Set<Book> findBookByUserId(Long id);
}
