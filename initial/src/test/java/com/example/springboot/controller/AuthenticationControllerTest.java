package com.example.springboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable login form redirection
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    // We simulate a user named "testUser" with the role "ADMIN"
    @WithMockUser(username = "testUser", authorities = {"ROLE_ADMIN"})
    public void testGetUserInfo() throws Exception {
        
        // ACT & ASSERT
        mockMvc.perform(get("/userinfo"))
                .andExpect(status().isOk())
                // Verify the response contains the username we mocked
                .andExpect(content().string(containsString("Authenticated Username: testUser")))
                // Verify the response contains the role we mocked
                .andExpect(content().string(containsString("ROLE_ADMIN")));
    }
}