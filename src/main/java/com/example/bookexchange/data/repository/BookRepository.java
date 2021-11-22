package com.example.bookexchange.data.repository;

import com.example.bookexchange.data.model.main.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>
{
    List<Book> findBookByNameLikeIgnoreCase(String input);
}
