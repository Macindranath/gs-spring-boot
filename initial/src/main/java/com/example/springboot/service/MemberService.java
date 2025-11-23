package com.example.springboot.service;

import com.example.springboot.repository.MemberRepository;
import java.util.List;
import com.example.springboot.model.Member;
import org.springframework.stereotype.Service;

// MemberService class for managing Member entities.
@Service
public class MemberService {

    
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Gets all archived members.
     * This now calls the repository method.
     * The database does all the work.
     */
    public List<Member> getArchivedMembers() {

        return this.memberRepository.findByArchivedAtIsNotNull();
    }

    /**
     * Gets all active (non-archived) members.
     * This also calls the repository method.
     */
    public List<Member> getActiveMembers() {

        return this.memberRepository.findByArchivedAtIsNull();
    }
}
