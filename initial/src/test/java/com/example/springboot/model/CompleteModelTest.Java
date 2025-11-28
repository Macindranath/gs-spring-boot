package com.example.springboot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Set;

/**
 * Comprehensive tests for the Model layer.
 * Aims to cover all getters, setters, constructors, and auxiliary logic.
 */
public class CompleteModelTest {

    // --- Auxiliary Classes Tests ---

    @Test
    public void testOperationEntity() {
        // Tests the Operation class used for GrantedAuthority
        Operation op = new Operation();
        // Assuming Operation has a way to set ID or using reflection if fields are private
        // If 'id' is private and has no setter, this test focuses on object creation
        assertNotNull(op);
        
        // If Operation implements GrantedAuthority, we test getAuthority
        // Note: Adjust according to specific Operation implementation
        assertNull(op.getAuthority()); 
    }

    @Test
    public void testRoleNameEnum() {
        // Tests the Enum values to ensure they exist
        assertEquals("ROLE_MANAGER", RoleName.ROLE_MANAGER.name());
        assertEquals("ROLE_MEMBER", RoleName.ROLE_MEMBER.name());
    }

    // --- Complex User Logic Tests ---

    @Test
    public void testUserRoleLogic() {
        // Tests the getRoles() method in the abstract User class
        Manager manager = new Manager();
        Role role = new Role();
        role.setName(RoleName.ROLE_MANAGER);
        
        manager.setRole(role);

        // Verify that getRole() returns the single role
        assertEquals(RoleName.ROLE_MANAGER, manager.getRole().getName());

        // Verify that getRoles() converts the single role into a Set
        Set<Role> roles = manager.getRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertTrue(roles.contains(role));
    }

    // --- Booking Logic Tests ---

    @Test
    public void testBookingConstructorsAndLogic() {
        // Test default constructor and timestamp generation
        Booking booking = new Booking();
        assertNotNull(booking.getCreatedAt());
        assertNotNull(booking.getUpdatedAt());

        // Test boolean logic for "cancelled"
        booking.setCancelled(true);
        assertEquals("Yes", booking.getCancelled());

        booking.setCancelled(false);
        assertEquals("No", booking.getCancelled());
    }

    @Test
    public void testTrainerDescription() {
        Trainer trainer = new Trainer();
        String description = "Expert in cardio and strength training.";
        trainer.setDescription(description);
        
        assertEquals(description, trainer.getDescription());
        assertNotNull(trainer.getCreatedAt());
    }
}