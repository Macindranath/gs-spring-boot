package com.example.springboot.controller;

import com.example.springboot.model.Court;
import com.example.springboot.repository.CourtRepository;
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

@WebMvcTest(CourtController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables login logic to focus on controller logic
public class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourtRepository courtRepository;

    // These are needed because Spring Security context often looks for them
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private com.example.springboot.repository.RoleRepository roleRepository;

    // --- 1. INDEX (Active vs Archived) ---

    @Test
    public void testIndexActive() throws Exception {
        // ARRANGE: Court without archivedAt date
        Court c = new Court();
        c.setId(1L);
        c.setArchivedAt(null);
        
        when(courtRepository.findAll()).thenReturn(Arrays.asList(c));

        // ACT & ASSERT
        mockMvc.perform(get("/court"))
                .andExpect(status().isOk())
                .andExpect(view().name("court/list"))
                .andExpect(model().attributeExists("courts"));
    }

    @Test
    public void testIndexArchived() throws Exception {
        // ARRANGE: Court WITH archivedAt date
        Court c = new Court();
        c.setId(2L);
        c.setArchivedAt(new Date());
        
        when(courtRepository.findAll()).thenReturn(Arrays.asList(c));

        // ACT & ASSERT: Request with param ?archived=1
        mockMvc.perform(get("/court").param("archived", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("court/list"))
                .andExpect(model().attributeExists("courts"));
    }

    // --- 2. DETAILS ---

    @Test
    public void testShowDetails() throws Exception {
        Court c = new Court();
        c.setId(1L);
        when(courtRepository.findById(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/court/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("court/view"))
                .andExpect(model().attributeExists("court"));
    }

    // --- 3. CREATE ---

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/court-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("court/create"));
    }

    @Test
    public void testSubmitCreateForm() throws Exception {
        mockMvc.perform(post("/court-create")
                .param("name", "Tennis Court 1")
                .param("hourlyRate", "50.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/court"));

        verify(courtRepository).save(any(Court.class));
    }

    // --- 4. EDIT & UPDATE ---

    @Test
    public void testShowEditForm() throws Exception {
        Court c = new Court();
        c.setId(1L);
        when(courtRepository.findById(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/court/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("court/edit"));
    }

    @Test
    public void testUpdateCourt() throws Exception {
        // ARRANGE
        // We MUST mock findById because your controller calls it 
        // to preserve the archivedAt date.
        Court existing = new Court();
        existing.setId(1L);
        when(courtRepository.findById(1L)).thenReturn(Optional.of(existing));

        // ACT
        mockMvc.perform(post("/court/1/edit")
                .param("name", "Updated Court")
                .param("hourlyRate", "60.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/court/1")); // Note: Your controller redirects to /court/{id}

        // ASSERT
        verify(courtRepository).save(any(Court.class));
    }

    // --- 5. ARCHIVE / UNARCHIVE ---

    @Test
    public void testArchiveCourt() throws Exception {
        Court c = new Court();
        c.setId(1L);
        when(courtRepository.findById(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/court/1/archive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/court"));

        verify(courtRepository).save(any(Court.class));
    }

    @Test
    public void testUnarchiveCourt() throws Exception {
        Court c = new Court();
        c.setId(1L);
        c.setArchivedAt(new Date());
        when(courtRepository.findById(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/court/1/unarchive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/court"));

        verify(courtRepository).save(any(Court.class));
    }
}
