package com.example.springboot.controller;

import com.example.springboot.model.Member;
import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;
import com.example.springboot.repository.MemberRepository;
import com.example.springboot.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables login requirement
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private MemberRepository memberRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    @Test
    public void testIndex() throws Exception {
        when(memberRepository.findAll()).thenReturn(Arrays.asList(new Member()));

        mockMvc.perform(get("/member"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/list"));
    }

    @Test
    // FIX: Simulates a user so 'userDetails' is not null in the controller
    @WithMockUser(username = "manager", roles = {"MANAGER"}) 
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/member-create"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/create"));
    }

    @Test
    public void testSubmitCreateForm() throws Exception {
        // ARRANGE
        // 1. Mock Role lookup (Critical!)
        Role role = new Role();
        role.setName(RoleName.ROLE_MEMBER);
        when(roleRepository.findByName(RoleName.ROLE_MEMBER)).thenReturn(Optional.of(role));
        
        // 2. Mock Password Encoder
        when(passwordEncoder.encode(any())).thenReturn("hashedPwd");

        // ACT
        mockMvc.perform(post("/member-create")
                .param("name", "New Member")
                .param("email", "test@test.com")
                .param("password", "123456")
                .param("phoneNumber", "123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member"));

        // ASSERT
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void testUpdateMember() throws Exception {
        // ARRANGE
        // Critical: Need to find a member so .getArchivedAt() doesn't crash
        Member existing = new Member();
        existing.setId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));

        // ACT
        mockMvc.perform(post("/member/1/edit")
                .param("name", "Updated Name")
                .param("email", "updated@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/1"));

        // ASSERT
        verify(memberRepository).save(any(Member.class));
    }
}