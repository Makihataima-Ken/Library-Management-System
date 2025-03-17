package com.library.demo.BorrowingRecord;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.demo.Book.Book;
import com.library.demo.Book.BookRepository;
import com.library.demo.Patron.Patron;
import com.library.demo.Patron.PatronRepository;

@Service
public class BorrowingService {
    
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;

    public BorrowingService(BorrowingRecordRepository borrowingRecordRepository,BookRepository bookRepository,PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    public String borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found"));

        // Check if book is already borrowed
        List<BorrowingRecord> activeRecords = borrowingRecordRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (!activeRecords.isEmpty()) {
            throw new RuntimeException("Book is already borrowed");
        }

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());

        borrowingRecordRepository.save(record);

        return "Book borrowed successfully.";
    }

    public String returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found for this book and patron"));

        record.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(record);

        return "Book returned successfully.";
    }
}
