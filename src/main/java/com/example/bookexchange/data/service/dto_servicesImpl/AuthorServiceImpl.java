package com.example.bookexchange.data.service.dto_servicesImpl;

import com.example.bookexchange.data.exception.AuthorNotFoundException;
import com.example.bookexchange.data.model.main.Author;
import com.example.bookexchange.data.repository.AuthorRepository;
import com.example.bookexchange.data.service.dto_services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService
{
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }



    // this method returns Author names while you are typing authors name anywhere
    @Override
    public List<Author> getSuggestedAuthors(String input)
    {
        List<Author> authors = authorRepository.findAuthorByNamesLikeIgnoreCase(input);

        if (authors.isEmpty()) throw new AuthorNotFoundException("---------Not exist----------");

        else return authors;
    }
}
