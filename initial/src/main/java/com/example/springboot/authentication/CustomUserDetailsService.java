package com.example.springboot.authentication;

import java.util.List;
import java.lang.Iterable;
import com.example.springboot.model.Member;
import java.util.stream.Collectors;
import com.example.springboot.model.Manager;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.example.springboot.repository.ManagerRepository;
import com.example.springboot.repository.MemberRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ManagerRepository managerRepository;
    private MemberRepository memberRepository;

    public CustomUserDetailsService(ManagerRepository managerRepository, MemberRepository memberRepository) {
        this.managerRepository = managerRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Iterable<Manager> managers = this.managerRepository.findAll();

        for (Manager manager : managers) {
            if (username.equals(manager.getEmail())) {
                List<GrantedAuthority> authorities = manager.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                return new User(manager.getEmail(), manager.getPassword(), authorities);
            }
        }

        Iterable<Member> members = this.memberRepository.findAll();

        for (Member member : members) {
            if (username.equals(member.getEmail())) {
                List<GrantedAuthority> authorities = member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                return new User(member.getEmail(), member.getPassword(), authorities);
            }
        }

        throw new UsernameNotFoundException("User with the following email address was not found.");
    }
}
