package com.example.springboot.controller;

import com.example.springboot.model.Booking;
import com.example.springboot.model.Member;
import com.example.springboot.repository.BookingRepository;
import com.example.springboot.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass login screen
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BookingRepository bookingRepository;

    // Even if not used directly, Spring Context often needs these beans to start
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private com.example.springboot.repository.RoleRepository roleRepository;

    // --- TEST 1: The Root URL ("/") ---
    @Test
    @WithMockUser
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    // --- TEST 2: The Logic Path (Member viewing their bookings) ---
    @Test
    @WithMockUser(username = "member@test.com")
    public void testHomeWithBookings() throws Exception {
        // 1. Setup the Current Member
        Member currentMember = new Member();
        currentMember.setId(1L);
        currentMember.setEmail("member@test.com");
        
        when(memberRepository.findByEmail("member@test.com")).thenReturn(Optional.of(currentMember));

        // 2. Setup Bookings (We create 3 scenarios to test your IF statement)
        
        // Scenario A: Valid Booking (Should be shown)
        Booking validBooking = new Booking();
        validBooking.setId(10L);
        validBooking.setMember(currentMember); // Matches current ID
        validBooking.setArchivedAt(null);      // Is Active

        // Scenario B: Archived Booking (Should be hidden)
        Booking archivedBooking = new Booking();
        archivedBooking.setId(20L);
        archivedBooking.setMember(currentMember);
        archivedBooking.setArchivedAt(new Date()); // Is Archived

        // Scenario C: Someone else's Booking (Should be hidden)
        Member otherMember = new Member();
        otherMember.setId(99L);
        Booking otherBooking = new Booking();
        otherBooking.setId(30L);
        otherBooking.setMember(otherMember); // Wrong ID
        otherBooking.setArchivedAt(null);

        // 3. Return all of them
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(validBooking, archivedBooking, otherBooking));

        // 4. Perform Request
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                // Expect only 1 booking in the list (Scenario A)
                .andExpect(model().attributeExists("myBookings"))
                .andExpect(model().attribute("myBookings", hasSize(1)));
    }

    // --- TEST 3: The Catch Block (Non-Member accessing home) ---
    @Test
    @WithMockUser(username = "manager@test.com")
    public void testHomeForNonMember() throws Exception {
        // ARRANGE
        // 1. Simulate a user who is NOT in the Member table (e.g. a Manager)
        when(memberRepository.findByEmail("manager@test.com")).thenReturn(Optional.empty());

        // 2. CRITICAL FIX: We must return at least one booking to force the 'for' loop to run.
        // If the list is empty, the loop is skipped, 'currentMember.get()' is never called,
        // and the exception is never thrown.
        Booking b = new Booking();
        b.setArchivedAt(null); // Must be active
        Member m = new Member(); m.setId(99L);
        b.setMember(m); // Must have a member
        
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(b));

        // ACT
        // Now the controller enters the loop -> checks the booking -> calls currentMember.get()
        // -> BOOM (NoSuchElementException) -> Catch Block -> returns "home" without adding model attribute.
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeDoesNotExist("myBookings"));
    }
}