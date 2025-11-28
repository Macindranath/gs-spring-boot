package com.example.springboot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

// This test increases Code Coverage by verifying
// that the POJOs (Plain Old Java Objects) work correctly.
public class ModelTest {

    // Tests the Manager entity's getters and setters
    @Test
    public void testManagerEntity() {
        Manager m = new Manager();
        m.setId(1L);
        m.setName("Mario Rossi");
        m.setEmail("mario@test.com");
        m.setPassword("pass123");
        
        assertEquals(1L, m.getId());
        assertEquals("Mario Rossi", m.getName());
        assertEquals("mario@test.com", m.getEmail());
        assertNotNull(m.getCreatedAt()); // Verifico che il costruttore metta la data
    }

    // Tests the Booking entity's getters and setters
    @Test
    public void testBookingEntity() {
        Booking b = new Booking();
        b.setId(100L);
        b.setDate("2025-05-20");
        b.setStartTime("10:00");
        b.setCancelled(true);

        assertEquals(100L, b.getId());
        assertEquals("2025-05-20", b.getDate());
        assertEquals("Yes", b.getCancelled()); // Tests the boolean logic
    }

    // Tests the Court entity's getters and setters
    @Test
    public void testCourtEntity() {
        Court c = new Court();
        c.setName("Campo A");
        c.setHourlyRate(50.0f);

        assertEquals("Campo A", c.getName());
        assertEquals(50.0f, c.getHourlyRate());
    }
}