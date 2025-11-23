package com.example.springboot.service;

import com.example.springboot.model.Manager;
import com.example.springboot.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

/**
 * Unit Test for ManagerService.
 * Tests the "Business Logic" layer.
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for this test class
public class ManagerServiceTest {

    // 1. Creation of a "Fake" Repository
    // I don't want to connect to the real database for a unit test.
    @Mock
    private ManagerRepository managerRepository;

    // 2. Injects the fake repository into the real Service
    @InjectMocks
    private ManagerService managerService;

    /**
     * Test: Does getActiveManagers() correctly ask the repository
     * for managers where archivedAt is NULL?
     */
    @Test
    public void testGetActiveManagers() {
        // --- ARRANGE (Setup) ---
        // Create a dummy manager to return
        Manager m1 = new Manager();
        m1.setName("Active Guy");
        List<Manager> fakeList = Arrays.asList(m1);

        // Tell the Mock: "If the service calls findByArchivedAtIsNull, give them this list."
        when(managerRepository.findByArchivedAtIsNull()).thenReturn(fakeList);

        // --- ACT (Run the method) ---
        List<Manager> result = managerService.getActiveManagers();

        // --- ASSERT (Verify results) ---
        // Did we get 1 manager back?
        assertEquals(1, result.size());
        // Was it the right manager?
        assertEquals("Active Guy", result.get(0).getName());

        // CRITICAL: Verify the service called the CORRECT repository method
        verify(managerRepository, times(1)).findByArchivedAtIsNull();
    }

    /**
     * Test: Does getArchivedManagers() correctly ask the repository
     * for managers where archivedAt is NOT NULL?
     */
    @Test
    public void testGetArchivedManagers() {
        // --- ARRANGE ---
        Manager m1 = new Manager();
        m1.setName("Archived Guy");
        List<Manager> fakeList = Arrays.asList(m1);

        // Tell the Mock: "If the service calls findByArchivedAtIsNotNull, give them this list."
        when(managerRepository.findByArchivedAtIsNotNull()).thenReturn(fakeList);

        // --- ACT ---
        List<Manager> result = managerService.getArchivedManagers();

        // --- ASSERT ---
        assertEquals(1, result.size());
        assertEquals("Archived Guy", result.get(0).getName());

        // CRITICAL: Verify the service called the CORRECT repository method
        verify(managerRepository, times(1)).findByArchivedAtIsNotNull();
    }
}