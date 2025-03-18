package com.library.demo.Patron;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import com.library.demo.Patron.Patron;
import com.library.demo.Patron.PatronRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest 
public class PatronRepositoryTest {
    
    @Autowired
    private PatronRepository patronRepository;

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp() {
        patron1 = new Patron("Patron 1", "stuff");
        patron2 = new Patron("Patron 2", "query stuff");
    }

    @Test
    public void testSaveAndFindById() {
        Patron savedPatron = patronRepository.save(patron1);
        Optional<Patron> foundPatron = patronRepository.findById(savedPatron.getId());

        assertTrue(foundPatron.isPresent());
        assertEquals(savedPatron.getName(), foundPatron.get().getName());
    }

    @Test
    public void testFindAll() {
        patronRepository.save(patron1);
        patronRepository.save(patron2);

        List<Patron> patrons = patronRepository.findAll();

        assertEquals(2, patrons.size());
        assertTrue(patrons.stream().anyMatch(b -> b.getName().equals("Patron 1")));
        assertTrue(patrons.stream().anyMatch(b -> b.getName().equals("Patron 2")));
    }

    @Test
    public void testDelete() {
        Patron savedpatron = patronRepository.save(patron1);
        Long id = savedpatron.getId();

        patronRepository.deleteById(id);

        Optional<Patron> deletedpatron = patronRepository.findById(id);
        assertFalse(deletedpatron.isPresent());
    }
}
