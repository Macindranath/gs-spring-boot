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
import com.example.springboot.repository.TrainerRepository;
import com.example.springboot.model.Trainer;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// CustomUserDetailsService class for loading user-specific data.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repositories for accessing user data
    private ManagerRepository managerRepository;
    private MemberRepository memberRepository;
    private TrainerRepository trainerRepository;

    public CustomUserDetailsService(ManagerRepository managerRepository, MemberRepository memberRepository, TrainerRepository trainerRepository) {
        this.managerRepository = managerRepository;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    // Override the loadUserByUsername method to load user details by username
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

        // Iterate through members to find a matching username
        Iterable<Member> members = this.memberRepository.findAll();
        
        for (Member member : members) {
            if (username.equals(member.getEmail())) {
                List<GrantedAuthority> authorities = member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                return new User(member.getEmail(), member.getPassword(), authorities);
            }
        }

        // Iterate through members to find a matching username
        Iterable<Trainer> trainers = this.trainerRepository.findAll();

        // If trainers is not null, iterate through trainers to find a matching username
        if (null != trainers) {
         for (Trainer trainer : trainers) {
            if (username.equals(trainer.getEmail())) {
                List<GrantedAuthority> authorities = trainer.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                return new User(trainer.getEmail(), trainer.getPassword(), authorities);
                }
            }
        }
        throw new UsernameNotFoundException("User with the following email address was not found.");
    }
}
