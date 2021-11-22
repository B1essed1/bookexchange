package com.example.bookexchange.data.service.dto_services;

import com.example.bookexchange.data.model.main.Book;

import java.util.List;

public interface BookService
{
    Book save (Book book);
    Book findById(Long id);
    Book deleteById(Long id);
    List<Book> findAllBook();
    List<Book> getSuggestedBooks(String input);


}
