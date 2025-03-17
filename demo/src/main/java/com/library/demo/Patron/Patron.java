package com.library.demo.Patron;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity  // This tells Spring Boot it's a JPA entity (maps to a database table)
@Table(name = "patrons")  // Optional: specify the table name
public class Patron {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Contact Information is mandatory")
    private String contactInformation;

    // Constructors
    public Patron() {}

    public Patron(String name, String contactInformation) {
        this.name = name;
        this.contactInformation = contactInformation;
    }
}
