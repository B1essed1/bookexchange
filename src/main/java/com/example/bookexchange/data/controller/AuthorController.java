package com.example.bookexchange.data.controller;

import com.example.bookexchange.data.model.main.Author;
import com.example.bookexchange.data.service.dto_services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v.1/user/authors/")
public class AuthorController
{

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("search/{input}")
    public ResponseEntity<List<Author>> searchByInputValuesAndGiveSuggestions(@PathVariable("input") String input)
    {
        List<Author> authors = new ArrayList<>();
        if (input.trim().length() >= 3)
        {
            authors = authorService.getSuggestedAuthors(input);
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } else return new ResponseEntity<>(authors, HttpStatus.OK);
    }
}
