package com.library.demo.Patron;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatronController.class)
public class PatronControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Autowired
    private ObjectMapper objectMapper;  // For JSON serialization

    private Patron patron1;
    private Patron patron2;

    @BeforeEach
    public void setUp() {
        patron1 = new Patron("Patron 1", "stuff");
        patron2 = new Patron("Patron 2", "stuff stuff");
    }

     @Test
    public void testCreatePatron() throws Exception {
        Mockito.when(patronService.savePatron(any(Patron.class))).thenReturn(patron1);

        mockMvc.perform(post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patron1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patron1.getId()))
                .andExpect(jsonPath("$.name").value(patron1.getName()))
                .andExpect(jsonPath("$.contactInformation").value(patron1.getContactInformation()));
    }

    @Test
    public void testGetAllPatrons() throws Exception {
        Mockito.when(patronService.getAllPatrons()).thenReturn(Arrays.asList(patron1, patron2));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(patron1.getId()))
                .andExpect(jsonPath("$[1].id").value(patron2.getId()));
    }

    @Test
    public void testGetPatronById_Found() throws Exception {
        Mockito.when(patronService.getPatronById(1L)).thenReturn(Optional.of(patron1));

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patron1.getId()))
                .andExpect(jsonPath("$.name").value(patron1.getName()))
                .andExpect(jsonPath("$.contactInformation").value(patron1.getContactInformation()));
    }

    @Test
    public void testGetPatronById_NotFound() throws Exception {
        Mockito.when(patronService.getPatronById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patrons/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePatron() throws Exception {
        Patron updatedPatron = new Patron("Updated Patron", "Updated stuff");
        Mockito.when(patronService.updatePatron(eq(1L), any(Patron.class))).thenReturn(updatedPatron);

        mockMvc.perform(put("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPatron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedPatron.getId()))
                .andExpect(jsonPath("$.name").value(updatedPatron.getName()))
                .andExpect(jsonPath("$.contactInformation").value(updatedPatron.getContactInformation()));
    }

    @Test
    public void testDeletePatron() throws Exception {
        Mockito.doNothing().when(patronService).deletePatron(1L);

        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isNoContent());
    }
}
