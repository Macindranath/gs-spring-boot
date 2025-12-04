package com.example.springboot.authentication;

import com.example.springboot.model.Manager;
import com.example.springboot.model.Member;
import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;
import com.example.springboot.repository.ManagerRepository;
import com.example.springboot.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.lang.NullPointerException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    public void testLoadUserByUsername_ManagerFound() {
        // ARRANGE
        String email = "manager@test.com";
        Manager manager = new Manager();
        manager.setEmail(email);
        manager.setPassword("pass");
        
        // Setup Role
        Role role = new Role();
        role.setName(RoleName.ROLE_MANAGER);
        manager.setRole(role);

        // IMPORTANT: The implementation uses findAll() and loops.
        // Must mock findAll returning a list containing the manager.
        when(managerRepository.findAll()).thenReturn(Arrays.asList(manager));

        // ACT
        UserDetails result = userDetailsService.loadUserByUsername(email);

        // ASSERT
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")));
    }

    @Test
    public void testLoadUserByUsername_MemberFound() {
        // ARRANGE
        String email = "member@test.com";
        Member member = new Member();
        member.setEmail(email);
        member.setPassword("pass");

        Role role = new Role();
        role.setName(RoleName.ROLE_MEMBER);
        member.setRole(role);

        // Manager list is empty, Member list contains the user
        when(managerRepository.findAll()).thenReturn(Collections.emptyList());
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member));

        // ACT
        UserDetails result = userDetailsService.loadUserByUsername(email);

        // ASSERT
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER")));
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        // ARRANGE
        // Both repositories return empty lists
        when(managerRepository.findAll()).thenReturn(Collections.emptyList());
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> {
            userDetailsService.loadUserByUsername("ghost@test.com");
        });
    }
}