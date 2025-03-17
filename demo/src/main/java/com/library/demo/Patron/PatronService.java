package com.library.demo.Patron;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatronService {

    @Autowired
    private PatronRepository PatronRepository;

    // GET all
    public List<Patron> getAllPatrons() {
        return PatronRepository.findAll();
    }

    // POST
    public Patron savePatron(Patron Patron) {
        return PatronRepository.save(Patron);
    }

    // GET
    public Optional<Patron> getPatronById(Long id) {
        return PatronRepository.findById(id);
    }

     // PUT
     public Patron updatePatron(Long id, Patron PatronDetails) {
        Patron existingPatron = PatronRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patron not found with id: " + id));

        existingPatron.setName(PatronDetails.getName());
        existingPatron.setContactInformation(PatronDetails.getContactInformation());

        return PatronRepository.save(existingPatron);
    }

    // DELETE
    public void deletePatron(Long id) {
        Patron existingPatron = PatronRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patron not found with id: " + id));

        PatronRepository.delete(existingPatron);
    }
}
