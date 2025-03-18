package com.library.demo.BorrowingRecord;

import com.library.demo.Book.Book;
import com.library.demo.Book.BookRepository;
import com.library.demo.Patron.Patron;
import com.library.demo.Patron.PatronRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // Loads only JPA components, uses in-memory DB (H2 by default)
public class BorrowingRecordRepositoryTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    private Book book;
    private Patron patron;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Test Author", 2020, "ISBN123");
        bookRepository.save(book);

        patron = new Patron("John Doe", "john@example.com");
        patronRepository.save(patron);

        BorrowingRecord record1 = new BorrowingRecord();
        record1.setBook(book);
        record1.setPatron(patron);
        record1.setBorrowDate(LocalDate.now());
        record1.setReturnDate(null); // Book not returned yet
        borrowingRecordRepository.save(record1);

        BorrowingRecord record2 = new BorrowingRecord();
        record2.setBook(book);
        record2.setPatron(patron);
        record2.setBorrowDate(LocalDate.now().minusDays(10));
        record2.setReturnDate(LocalDate.now().minusDays(2)); // Book returned
        borrowingRecordRepository.save(record2);
    }

    @Test
    void testFindByBookIdAndReturnDateIsNull() {
        List<BorrowingRecord> activeRecords = borrowingRecordRepository.findByBookIdAndReturnDateIsNull(book.getId());
        assertEquals(1, activeRecords.size());
        assertNull(activeRecords.get(0).getReturnDate());  // confirm returnDate is null
    }

    @Test
    void testFindByBookIdAndPatronIdAndReturnDateIsNull_Found() {
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(book.getId(), patron.getId());
        assertTrue(recordOpt.isPresent());
        assertNull(recordOpt.get().getReturnDate());
    }

    @Test
    void testFindByBookIdAndPatronIdAndReturnDateIsNull_NotFound() {
        // Return date is not null, so this query should return empty
        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(999L, patron.getId());
        assertFalse(recordOpt.isPresent());
    }
}
