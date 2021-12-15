package com.example.bookexchange.data.controller;


import com.example.bookexchange.data.exception.BookNotFoundException;
import com.example.bookexchange.data.model.main.Book;
import com.example.bookexchange.data.service.dto_services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


// every operation on book must do in this class
@RestController
@Slf4j
@RequestMapping("/api/v.1/user/books/")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("all")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> bookList = bookService.getAllBooks();

        return ResponseEntity.ok(bookList);
    }

    // this method saves books that created first time;
    @PostMapping("create")
    public ResponseEntity<Book> saveBooks(@RequestBody Book book) {

        Book books = bookService.save(book);

        if (books == null) {
            throw new BookNotFoundException("topilmadi kitob ");
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(books.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //this method returs suggested books by its name ,
    @GetMapping("search")
    public ResponseEntity<List<Book>> searchByInputValueAndGiveSuggestions(@RequestParam("name") String input) {
        List<Book> books = new ArrayList<>();

        System.out.println(input);
        log.info(input);
        log.debug(input);
        if (input.trim().length() >= 3) {
            books = bookService.getSuggestedBooks(input);
            if (books.isEmpty()) throw new BookNotFoundException("---------any similar Book Not Found----");
            log.error("any similar book not found");
            log.info("any similar book not found");
            return new ResponseEntity(books, HttpStatus.OK);
        }
        else return new ResponseEntity<>(books, HttpStatus.BAD_REQUEST);

        //checks input, if input's size greater than 3
        // it starts searching from db which look alike theese input value

        // if check is not greater than 3 it returns empty List with 200 response
    }

    @PutMapping("update")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book shouldUpdateBook = bookService.findById(book.getId());

        if (shouldUpdateBook == null) throw new BookNotFoundException("Book Not Found");
        else {

            shouldUpdateBook.setName(book.getName());
            shouldUpdateBook.setAuthors(book.getAuthors());
            shouldUpdateBook.setCoverType(book.getCoverType());
            shouldUpdateBook.setDescription(book.getDescription());
            shouldUpdateBook.setGenres(book.getGenres());
            shouldUpdateBook.setLanguage(book.getLanguage());
            shouldUpdateBook.setQuantity(book.getQuantity());
            shouldUpdateBook.setPublishDate(book.getPublishDate());
            shouldUpdateBook.setSize(book.getSize());
            bookService.save(shouldUpdateBook);
        }
        return new ResponseEntity(shouldUpdateBook, HttpStatus.OK);
    }

    @GetMapping("userbooks/{id}")
    public ResponseEntity<Set<Book>> getProfilesBooks(@PathVariable("id") Long id) {
        Set<Book> books = bookService.getBookByUserId(id);
        return ResponseEntity.ok(books);
    }

    @GetMapping("get/{id}")
    public  ResponseEntity<Book> getBookById(@PathVariable("id") Long id){

        Book book = bookService.findById(id);
        if(book == null) throw  new BookNotFoundException("Book does not exist ");
        else return ResponseEntity.ok(book);
    }
}
