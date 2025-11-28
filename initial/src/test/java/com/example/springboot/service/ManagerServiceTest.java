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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ManagerService.
 * Requires both tests to run to achieve 100% coverage.
 */
@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerService managerService;

    @Test
    public void testGetActiveManagers() {
        // ARRANGE
        Manager m = new Manager();
        m.setName("Active Manager");
        List<Manager> expectedList = Arrays.asList(m);

        when(managerRepository.findByArchivedAtIsNull()).thenReturn(expectedList);

        // ACT
        List<Manager> result = managerService.getActiveManagers();

        // ASSERT
        assertEquals(1, result.size());
        verify(managerRepository, times(1)).findByArchivedAtIsNull();
    }

    @Test
    public void testGetArchivedManagers() {
        // ARRANGE
        Manager m = new Manager();
        m.setName("Archived Manager");
        List<Manager> expectedList = Arrays.asList(m);

        when(managerRepository.findByArchivedAtIsNotNull()).thenReturn(expectedList);

        // ACT
        List<Manager> result = managerService.getArchivedManagers();

        // ASSERT
        assertEquals(1, result.size());
        verify(managerRepository, times(1)).findByArchivedAtIsNotNull();
    }
}