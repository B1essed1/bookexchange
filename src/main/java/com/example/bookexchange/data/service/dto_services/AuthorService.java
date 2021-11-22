package com.example.bookexchange.data.service.dto_services;

import com.example.bookexchange.data.model.main.Author;

import java.util.List;

public interface AuthorService
{
    List<Author> getSuggestedAuthors(String input);
}
