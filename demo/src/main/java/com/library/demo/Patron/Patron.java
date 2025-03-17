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

    //setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
