package com.library.demo.Patron;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Auto-initialize mocks
public class PatronServiceTest {
    
    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp() {
        patron1 = new Patron("Patron 1", "stuff");
        patron1.setId(1L);

        patron2 = new Patron("Patron 2", "stuff stuff");
        patron2.setId(2L);
    }

    @Test
    public void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(Arrays.asList(patron1, patron2));

        List<Patron> result = patronService.getAllPatrons();
        assertEquals(2, result.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    public void testSavePatron() {
        when(patronRepository.save(patron1)).thenReturn(patron1);

        Patron saved = patronService.savePatron(patron1);
        assertEquals(patron1.getName(), saved.getName());
        verify(patronRepository, times(1)).save(patron1);
    }

    @Test
    public void testGetPatronById_Found() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));

        Optional<Patron> found = patronService.getPatronById(1L);
        assertTrue(found.isPresent());
        assertEquals("Patron 1", found.get().getName());
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPatronById_NotFound() {
        when(patronRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Patron> found = patronService.getPatronById(99L);
        assertFalse(found.isPresent());
        verify(patronRepository, times(1)).findById(99L);
    }

    @Test
    public void testUpdatePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron1);

        Patron updatedDetails = new Patron("Updated Patron", "Updated stuff");
        Patron updatedPatron = patronService.updatePatron(1L, updatedDetails);

        assertEquals("Updated Patron", updatedPatron.getName());
        assertEquals("Updated stuff", updatedPatron.getContactInformation());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(patron1);
    }

    @Test
    public void testUpdatePatron_NotFound() {
        when(patronRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patronService.updatePatron(99L, patron1));

        assertTrue(exception.getMessage().contains("Patron not found with id: 99"));
        verify(patronRepository, times(1)).findById(99L);
    }

    @Test
    public void testDeletePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));
        doNothing().when(patronRepository).delete(patron1);

        patronService.deletePatron(1L);
        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).delete(patron1);
    }

    @Test
    public void testDeletePatron_NotFound() {
        when(patronRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patronService.deletePatron(99L));

        assertTrue(exception.getMessage().contains("Patron not found with id: 99"));
        verify(patronRepository, times(1)).findById(99L);
    }
}
