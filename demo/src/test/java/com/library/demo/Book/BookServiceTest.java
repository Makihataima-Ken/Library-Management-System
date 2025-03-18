package com.library.demo.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Auto-initialize mocks
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book("Book Title 1", "Author 1", 2000, "123456");
        book1.setId(1L);

        book2 = new Book("Book Title 2", "Author 2", 1995, "654321");
        book2.setId(2L);
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testSaveBook() {
        when(bookRepository.save(book1)).thenReturn(book1);

        Book saved = bookService.saveBook(book1);
        assertEquals(book1.getTitle(), saved.getTitle());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> found = bookService.getBookById(1L);
        assertTrue(found.isPresent());
        assertEquals("Book Title 1", found.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> found = bookService.getBookById(99L);
        assertFalse(found.isPresent());
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    public void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book updatedDetails = new Book("Updated Title", "Updated Author", 2010, "111111");
        Book updatedBook = bookService.updateBook(1L, updatedDetails);

        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Updated Author", updatedBook.getAuthor());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void testUpdateBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookService.updateBook(99L, book1));

        assertTrue(exception.getMessage().contains("Book not found with id: 99"));
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    public void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        doNothing().when(bookRepository).delete(book1);

        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(book1);
    }

    @Test
    public void testDeleteBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookService.deleteBook(99L));

        assertTrue(exception.getMessage().contains("Book not found with id: 99"));
        verify(bookRepository, times(1)).findById(99L);
    }
}
