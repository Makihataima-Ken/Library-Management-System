package com.library.demo.Patron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    @Autowired
    private PatronService PatronService;

    // Create a new Patron
    @PostMapping
    public ResponseEntity<Patron> createPatron(@RequestBody Patron Patron) {
        Patron savedPatron = PatronService.savePatron(Patron);
        return ResponseEntity.ok(savedPatron);
    }

    // Get all Patrons
    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> Patrons = PatronService.getAllPatrons();
        return ResponseEntity.ok(Patrons);
    }

    // Get a Patron by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        return PatronService.getPatronById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a Patron
    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron PatronDetails) {
        Patron updatedPatron = PatronService.updatePatron(id, PatronDetails);
        return ResponseEntity.ok(updatedPatron);
    }

    // Delete a Patron
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        PatronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
