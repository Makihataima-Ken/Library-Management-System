package com.library.demo.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    // GET all
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // POST
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // GET
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

     // PUT
     public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPublicationYear(bookDetails.getPublicationYear());

        return bookRepository.save(existingBook);
    }

    // DELETE
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        bookRepository.delete(existingBook);
    }
}
