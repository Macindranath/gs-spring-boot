package com.example.springboot.factory;

import com.example.springboot.model.Manager;
import com.example.springboot.model.Member;
import com.example.springboot.model.Trainer;
import com.example.springboot.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Abstract Factory implementation.
 * Ensures all concrete factories produce the correct object types.
 */
public class CompleteFactoryTest {

    @Test
    public void testAllFactories() {
        // 1. Test ManagerFactory
        UserFactory managerFactory = new ManagerFactory();
        User manager = managerFactory.getInstance();
        assertNotNull(manager);
        assertTrue(manager instanceof Manager, "Factory should return a Manager instance");

        // 2. Test MemberFactory 
        UserFactory memberFactory = new MemberFactory();
        User member = memberFactory.getInstance();
        assertNotNull(member);
        assertTrue(member instanceof Member, "Factory should return a Member instance");

        // 3. Test TrainerFactory
        UserFactory trainerFactory = new TrainerFactory();
        User trainer = trainerFactory.getInstance();
        assertNotNull(trainer);
        assertTrue(trainer instanceof Trainer, "Factory should return a Trainer instance");
    }
}