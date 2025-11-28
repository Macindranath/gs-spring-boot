package com.example.springboot.authentication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationProviderTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private CustomAuthenticationProvider authProvider;

    @Test
    public void testAuthenticate_HardcodedAdmin() {
        // ARRANGE
        Authentication inputAuth = new UsernamePasswordAuthenticationToken("administrator", "passwordpassword");

        // ACT
        Authentication result = authProvider.authenticate(inputAuth);

        // ASSERT
        assertNotNull(result);
        assertEquals("administrator", result.getName());
        // Verify no DB calls were made for the hardcoded admin
        verifyNoInteractions(userDetailsService);
    }

    @Test
    public void testAuthenticate_StandardUser_Success() {
        // ARRANGE
        String username = "user@test.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        // Mock UserDetails
        UserDetails mockUser = mock(UserDetails.class);
        when(mockUser.getPassword()).thenReturn(encodedPassword);
        when(mockUser.getUsername()).thenReturn(username);

        // Mock Service and Encoder behavior
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        Authentication inputAuth = new UsernamePasswordAuthenticationToken(username, rawPassword);

        // ACT
        Authentication result = authProvider.authenticate(inputAuth);

        // ASSERT
        assertNotNull(result);
        assertEquals(username, result.getName());
    }

    @Test
    public void testAuthenticate_StandardUser_BadPassword() {
        // ARRANGE
        String username = "user@test.com";
        String wrongPassword = "wrong";
        String encodedPassword = "encoded";

        UserDetails mockUser = mock(UserDetails.class);
        when(mockUser.getPassword()).thenReturn(encodedPassword);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUser);
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        Authentication inputAuth = new UsernamePasswordAuthenticationToken(username, wrongPassword);

        // ACT & ASSERT
        assertThrows(BadCredentialsException.class, () -> {
            authProvider.authenticate(inputAuth);
        });
    }

    @Test
    public void testSupports() {
        assertTrue(authProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(authProvider.supports(String.class));
    }
}