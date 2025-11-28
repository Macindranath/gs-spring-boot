package com.example.springboot.controller;

import com.example.springboot.model.Manager;
import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;
import com.example.springboot.repository.ManagerRepository;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.service.ManagerService; // Import the Service
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

@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- MOCK ALL DEPENDENCIES INJECTED IN CONSTRUCTOR ---
    @MockBean private ManagerRepository managerRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private ManagerService managerService; // <--- Critical for index()

    // --- 1. INDEX (Tests the Service calls) ---

    @Test
    public void testIndexActive() throws Exception {
        // The controller calls managerService.getActiveManagers()
        when(managerService.getActiveManagers()).thenReturn(Arrays.asList(new Manager()));

        mockMvc.perform(get("/manager"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/list"))
                .andExpect(model().attributeExists("managers"));
    }

    @Test
    public void testIndexArchived() throws Exception {
        // The controller calls managerService.getArchivedManagers()
        when(managerService.getArchivedManagers()).thenReturn(Arrays.asList(new Manager()));
        
        mockMvc.perform(get("/manager").param("archived", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/list"));
    }

    // --- 2. CREATE ---

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/manager-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/create"));
    }

    @Test
    public void testSubmitCreateForm() throws Exception {
        // Setup Role and Password logic
        Role role = new Role();
        role.setName(RoleName.ROLE_MANAGER);
        when(roleRepository.findByName(RoleName.ROLE_MANAGER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");

        mockMvc.perform(post("/manager-create")
                .param("name", "New Boss")
                .param("password", "123")
                .param("email", "boss@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager"));
                
        verify(managerRepository).save(any(Manager.class));
    }

    // --- 3. DETAILS & EDIT ---

    @Test
    public void testShowDetails() throws Exception {
        Manager m = new Manager();
        m.setId(1L);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/manager/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/view"));
    }

    @Test
    public void testShowEditForm() throws Exception {
        Manager m = new Manager();
        m.setId(1L);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/manager/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/edit"));
    }

    @Test
    public void testUpdateManager() throws Exception {
        // We mock findById because the controller calls .getArchivedAt() on the result
        Manager existing = new Manager();
        existing.setId(1L);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/manager/1/edit")
                .param("name", "Updated Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/1"));
        
        verify(managerRepository).save(any(Manager.class));
    }

    // --- 4. ARCHIVE / UNARCHIVE ---

    @Test
    public void testArchiveActions() throws Exception {
        Manager m = new Manager();
        m.setId(1L);
        // Return a real object so .setArchivedAt() doesn't crash
        when(managerRepository.findById(1L)).thenReturn(Optional.of(m));

        // Archive
        mockMvc.perform(get("/manager/1/archive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager"));

        // Unarchive
        mockMvc.perform(get("/manager/1/unarchive"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager"));
                
        // Verify save was called 2 times (once for archive, once for unarchive)
        verify(managerRepository, org.mockito.Mockito.times(2)).save(any(Manager.class));
    }
}