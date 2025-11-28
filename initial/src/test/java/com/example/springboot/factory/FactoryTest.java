package com.example.springboot.factory;

import com.example.springboot.model.Manager;
import com.example.springboot.model.Member;
import com.example.springboot.model.Trainer;
import com.example.springboot.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the Abstract Factory implementation.
public class FactoryTest {

    // Tests the ManagerFactory produces a Manager instance
    @Test
    public void testManagerFactory() {
        UserFactory factory = new ManagerFactory();
        User user = factory.getInstance();

        assertNotNull(user);
        assertTrue(user instanceof Manager);
    }

    // Tests the MemberFactory produces a Member instance
    @Test
    public void testMemberFactory() {
        UserFactory factory = new MemberFactory();
        User user = factory.getInstance();

        assertNotNull(user);
        assertTrue(user instanceof Member);
    }

    // Tests the TrainerFactory produces a Trainer instance
    @Test
    public void testTrainerFactory() {
        UserFactory factory = new TrainerFactory();
        User user = factory.getInstance();

        assertNotNull(user);
        assertTrue(user instanceof Trainer);
    }
}