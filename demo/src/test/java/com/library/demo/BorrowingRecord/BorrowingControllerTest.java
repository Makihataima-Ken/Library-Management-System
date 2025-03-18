package com.library.demo.BorrowingRecord;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(BorrowingController.class)
public class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingService borrowingService;

    @Test
    public void testBorrowBook_Success() throws Exception {
        Mockito.when(borrowingService.borrowBook(anyLong(), anyLong()))
               .thenReturn("Book borrowed successfully.");

        mockMvc.perform(post("/api/borrow/1/patron/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully."));
    }

    @Test
    public void testReturnBook_Success() throws Exception {
        Mockito.when(borrowingService.returnBook(anyLong(), anyLong()))
               .thenReturn("Book returned successfully.");

        mockMvc.perform(put("/api/return/1/patron/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully."));
    }

    @Test
    public void testBorrowBook_Failure() throws Exception {
        Mockito.when(borrowingService.borrowBook(1l,1l))
               .thenThrow(new RuntimeException("Book is already borrowed"));

        mockMvc.perform(post("/api/borrow/1l/patron/1l")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());  
    }

    @Test
    public void testReturnBook_Failure() throws Exception {
        Mockito.when(borrowingService.returnBook(anyLong(), anyLong()))
               .thenThrow(new RuntimeException("No active borrowing record found"));

        mockMvc.perform(put("/api/return/1l/patron/1l")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());  
    }
}
