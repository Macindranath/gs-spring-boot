package com.example.springboot.service;

import com.example.springboot.repository.MemberRepository;
import java.util.List;
import java.util.ArrayList;
import com.example.springboot.model.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    // Dependency Injection
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getArchivedMembers() {
        List<Member> archivedMembers = new ArrayList<Member>();

        Iterable<Member> members = this.memberRepository.findAll();

        for (Member member : members) {
            if (null != member.getArchivedAt()) {
                archivedMembers.add(member);
            }
        }

        return archivedMembers;
    }

    public List<Member> getActiveMembers() {
        List<Member> activeMembers = new ArrayList<Member>();

        Iterable<Member> members = this.memberRepository.findAll();

        for (Member member : members) {
            if (null == member.getArchivedAt()) {
                activeMembers.add(member);
            }
        }

        return activeMembers;
    }
}
