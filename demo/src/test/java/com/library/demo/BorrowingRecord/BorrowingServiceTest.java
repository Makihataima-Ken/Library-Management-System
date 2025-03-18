package com.library.demo.BorrowingRecord;

import com.library.demo.Book.Book;
import com.library.demo.Book.BookRepository;
import com.library.demo.Patron.Patron;
import com.library.demo.Patron.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
class BorrowingServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    private Book book;
    private Patron patron;

    @BeforeEach
    void setUp() {
        book = new Book("Test Title", "Test Author", 2020, "ISBN123");
        book.setId(1L);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Test Patron");
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(1L)).thenReturn(Collections.emptyList());
        when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = borrowingService.borrowBook(1L, 1L);
        assertEquals("Book borrowed successfully.", result);

        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void testBorrowBook_AlreadyBorrowed() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        BorrowingRecord activeRecord = new BorrowingRecord();
        activeRecord.setBook(book);
        activeRecord.setPatron(patron);
        activeRecord.setBorrowDate(LocalDate.now());

        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(1L))
                .thenReturn(Collections.singletonList(activeRecord));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Book is already borrowed", ex.getMessage());
    }

    @Test
    void testReturnBook_Success() {
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(record));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = borrowingService.returnBook(1L, 1L);
        assertEquals("Book returned successfully.", result);
        assertNotNull(record.getReturnDate());

        verify(borrowingRecordRepository, times(1)).save(record);
    }

    @Test
    void testReturnBook_NoActiveRecord() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> borrowingService.returnBook(1L, 1L));
        assertEquals("No active borrowing record found for this book and patron", ex.getMessage());
    }
}
