package com.example.springboot.service;

import com.example.springboot.model.Trainer;
import com.example.springboot.repository.TrainerRepository;
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

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    public void testGetActiveTrainers() {
        Trainer t = new Trainer();
        List<Trainer> list = Arrays.asList(t);

        when(trainerRepository.findByArchivedAtIsNull()).thenReturn(list);

        List<Trainer> result = trainerService.getActiveTrainers();

        assertEquals(1, result.size());
        verify(trainerRepository, times(1)).findByArchivedAtIsNull();
    }

    @Test
    public void testGetArchivedTrainers() {
        Trainer t = new Trainer();
        List<Trainer> list = Arrays.asList(t);

        when(trainerRepository.findByArchivedAtIsNotNull()).thenReturn(list);

        List<Trainer> result = trainerService.getArchivedTrainers();

        assertEquals(1, result.size());
        verify(trainerRepository, times(1)).findByArchivedAtIsNotNull();
    }
}