package com.example.springboot.service;

import com.example.springboot.model.Member;
import com.example.springboot.repository.MemberRepository;
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
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void testGetActiveMembers() {
        Member m = new Member();
        List<Member> list = Arrays.asList(m);
        
        when(memberRepository.findByArchivedAtIsNull()).thenReturn(list);

        List<Member> result = memberService.getActiveMembers();

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findByArchivedAtIsNull();
    }

    @Test
    public void testGetArchivedMembers() {
        Member m = new Member();
        List<Member> list = Arrays.asList(m);

        when(memberRepository.findByArchivedAtIsNotNull()).thenReturn(list);

        List<Member> result = memberService.getArchivedMembers();

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findByArchivedAtIsNotNull();
    }
}