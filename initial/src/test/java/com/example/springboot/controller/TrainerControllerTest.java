package com.example.springboot.controller;

import com.example.springboot.model.Trainer;
import com.example.springboot.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private TrainerRepository trainerRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    // --- 1. INDEX (Active vs Archived) ---

    @Test
    public void testIndexDefault() throws Exception {
        // Test default view (Active trainers)
        Trainer t = new Trainer();
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(t));

        mockMvc.perform(get("/trainer"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainer/list"))
                .andExpect(model().attributeExists("trainers"));
    }

    @Test
    public void testIndexArchived() throws Exception {
        // Test the "Archived" view logic (if (1 == archived))
        Trainer t = new Trainer();
        t.setArchivedAt(new Date()); // It's archived
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(t));

        mockMvc.perform(get("/trainer").param("archived", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainer/list"))
                .andExpect(model().attributeExists("trainers"));
    }

    // --- 2. CREATE ---

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/trainer-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainer/create"));
    }

    @Test
    public void testSubmitCreateForm() throws Exception {
        mockMvc.perform(post("/trainer-create")
                .param("name", "New Trainer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainer"));
        
        verify(trainerRepository).save(any(Trainer.class));
    }

    // --- 3. EDIT & UPDATE ---

    @Test
    public void testShowEditForm() throws Exception {
        Trainer t = new Trainer();
        t.setId(1L);
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/trainer/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainer/edit"));
    }

    @Test
    public void testUpdateTrainer() throws Exception {
        Trainer existing = new Trainer();
        existing.setId(1L);
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/trainer/1/edit")
                .param("name", "Updated Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainer/1"));

        verify(trainerRepository).save(any(Trainer.class));
    }

    // --- 4. ARCHIVE / UNARCHIVE ---

    @Test
    public void testArchiveTrainer() throws Exception {
        Trainer t = new Trainer();
        t.setId(1L);
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/trainer/1/archive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainer"));

        // Verify we saved the change
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    public void testUnarchiveTrainer() throws Exception {
        Trainer t = new Trainer();
        t.setId(1L);
        t.setArchivedAt(new Date());
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/trainer/1/unarchive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trainer"));

        verify(trainerRepository).save(any(Trainer.class));
    }
    
    @Test
    public void testShowDetails() throws Exception {
        Trainer t = new Trainer();
        t.setId(1L);
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/trainer/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainer/view"));
    }
}