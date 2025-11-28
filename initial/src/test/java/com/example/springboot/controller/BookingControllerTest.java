package com.example.springboot.controller;

import com.example.springboot.model.Booking;
import com.example.springboot.model.Court;
import com.example.springboot.model.Member;
import com.example.springboot.repository.BookingRepository;
import com.example.springboot.repository.CourtRepository;
import com.example.springboot.repository.MemberRepository;
import com.example.springboot.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingRepository bookingRepository;
    @MockBean private CourtRepository courtRepository;
    @MockBean private MemberRepository memberRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    // --- 1. LIST (Active vs Archived) ---

    @Test
    public void testIndexActive() throws Exception {
        Booking b = new Booking();
        b.setArchivedAt(null);
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(b));

        mockMvc.perform(get("/booking"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/list"))
                .andExpect(model().attributeExists("bookings"));
    }

    @Test
    public void testIndexArchived() throws Exception {
        Booking b = new Booking();
        b.setArchivedAt(new Date());
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(b));

        mockMvc.perform(get("/booking").param("archived", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/list"));
    }

    // --- 2. SHOW DETAILS ---

    @Test
    public void testShowDetails() throws Exception {
        // ARRANGE
        Booking b = new Booking();
        b.setId(1L);
        
        // Setup Court and Member to avoid Template Errors
        Court c = new Court(); c.setName("Test Court");
        b.setCourt(c);
        
        Member m = new Member(); m.setName("Test Member");
        b.setMember(m);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));

        // ACT & ASSERT
        mockMvc.perform(get("/booking/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/view"))
                .andExpect(model().attributeExists("booking"));
    }

    // --- 3. SHOW CREATE FORM ---

    @Test
    @WithMockUser(username = "manager") 
    public void testShowCreateForm() throws Exception {
        when(memberRepository.findByEmail("manager")).thenReturn(Optional.empty());
        when(courtRepository.findAll()).thenReturn(new ArrayList<>());
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/booking-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/create"));
    }

    // --- 4. SUBMIT FORM (Happy Path) ---

    @Test
    @WithMockUser(username = "manager")
    public void testSubmitFormSuccess() throws Exception {
        Court c = new Court(); c.setId(10L);
        Member m = new Member(); m.setId(20L);

        when(courtRepository.findById(10L)).thenReturn(Optional.of(c));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(m));
        
        when(memberRepository.findByEmail("manager")).thenReturn(Optional.empty());
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/booking-create")
                .param("courtId", "10")
                .param("memberId", "20")
                .param("date", "2025-10-10")
                .param("startTime", "10:00")
                .param("endTime", "11:00")
                .param("cancelled", "No"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking"));

        verify(bookingRepository).save(any(Booking.class));
    }

    // --- 5. SUBMIT FORM (Conflict/Error Path) ---

    @Test
    @WithMockUser(username = "member@test.com")
    public void testSubmitFormWithTimeConflict() throws Exception {
        Court c = new Court(); c.setId(10L);
        Member m = new Member(); m.setId(20L);
        when(courtRepository.findById(10L)).thenReturn(Optional.of(c));
        
        when(memberRepository.findByEmail("member@test.com")).thenReturn(Optional.of(m));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(m));

        // CONFLICT SETUP
        Booking existing = new Booking();
        existing.setCourt(c);
        existing.setDate("2025-10-10");
        existing.setStartTime("10:00");
        existing.setEndTime("11:00");
        // FIX: Use boolean false
        existing.setCancelled(false); 

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(existing));

        // ACT
        mockMvc.perform(post("/booking-create")
                .param("courtId", "10")
                .param("memberId", "20")
                .param("date", "2025-10-10") 
                .param("startTime", "10:00") 
                .param("endTime", "11:00")
                .param("cancelled", "false")) 
                .andExpect(status().isOk()) 
                .andExpect(view().name("booking/create")) 
                .andExpect(model().attributeExists("bookingError")); 

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    // --- 6. CANCEL ---

    @Test
    public void testCancelBooking() throws Exception {
        Booking b = new Booking();
        b.setId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));

        mockMvc.perform(get("/booking/1/cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking"));

        verify(bookingRepository).save(any(Booking.class));
    }
}