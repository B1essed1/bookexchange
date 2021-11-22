package com.example.bookexchange.data.service.dto_servicesImpl;

import com.example.bookexchange.data.exception.BookNotFoundException;
import com.example.bookexchange.data.model.main.Book;
import com.example.bookexchange.data.repository.BookRepository;
import com.example.bookexchange.data.service.dto_services.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService
{
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book)
    {
        if (book != null)
        return bookRepository.save(book);
        else throw new BookNotFoundException("--------Book not Found---------");
    }

    @Override
    public Book findById(Long id)
    {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty())
        {
            return book.get();
        } else throw new BookNotFoundException("-----Book Not Found-----");

    }

    @Override
    public Book deleteById(Long id)
    {
        return null;
    }

    @Override
    public List<Book> findAllBook()
    {
        List<Book> books = new ArrayList<>();

        bookRepository.findAll().forEach(books::add);
        return books;
    }

    // this method returns suggested books while user searching or posting books by its name
    @Override
    public List<Book> getSuggestedBooks(String input)
    {
        List<Book> suggestedBooks = bookRepository.findBookByNameLikeIgnoreCase(input);

        if (suggestedBooks.isEmpty())
        {
            throw  new BookNotFoundException("------ this kind of book has not its similarity ------ ");
        } else
        {
            return suggestedBooks;
        }
    }
}
