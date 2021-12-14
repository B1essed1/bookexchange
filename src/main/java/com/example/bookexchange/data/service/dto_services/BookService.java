package com.example.bookexchange.data.service.dto_services;

import com.example.bookexchange.data.model.main.Book;

import java.util.List;
import java.util.Set;

public interface BookService {
    Book save (Book book);
    Book findById(Long id);
    Set<Book> getBookByUserId(Long id);
    List<Book> getSuggestedBooks(String input);
    List<Book> getAllBooks();
}
