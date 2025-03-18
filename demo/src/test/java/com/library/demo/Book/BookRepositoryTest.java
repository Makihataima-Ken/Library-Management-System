package com.library.demo.Book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Spins up in-memory DB and configures Spring Data JPA
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book("Book Title 1", "Author 1", 2000, "1234567890");
        book2 = new Book("Book Title 2", "Author 2", 1999, "0987654321");
    }

    @Test
    public void testSaveAndFindById() {
        Book savedBook = bookRepository.save(book1);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(savedBook.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void testFindAll() {
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book Title 1")));
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book Title 2")));
    }

    @Test
    public void testDelete() {
        Book savedBook = bookRepository.save(book1);
        Long id = savedBook.getId();

        bookRepository.deleteById(id);

        Optional<Book> deletedBook = bookRepository.findById(id);
        assertFalse(deletedBook.isPresent());
    }
}

