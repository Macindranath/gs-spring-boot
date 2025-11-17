package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import com.example.springboot.repository.MemberRepository;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.model.Member;
import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.ArrayList;
import java.lang.Iterable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Handles all web (HTTP) requests related to Member users.
 * This includes displaying lists, handling creation, editing, and archiving.
 */

@Controller
public class MemberController {

    private MemberRepository memberRepository;

    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberController(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Displays a list of members.
     * It can show 'active' (default) or 'archived' members based on the 'archived' URL parameter.
     */
    
    @GetMapping("/member")
    public String index(Model model, @RequestParam(defaultValue = "0") int archived) {
        List<Member> result = new ArrayList<Member>();

        Iterable<Member> members = this.memberRepository.findAll();

        for (Member member : members) {
            if (1 == archived) {
                if (null != member.getArchivedAt()) {
                    result.add(member);
                }
            } else {
                if (null == member.getArchivedAt()) {
                    result.add(member);
                }
            }
        }

        model.addAttribute("members", result);

        return "member/list";
    }

    @GetMapping("/member/{id}")
    public String show(@PathVariable Long id, Model model) {
        Member member = this.memberRepository.findById(id).orElse(null);

        model.addAttribute("member", member);

        return "member/view";
    }

    @PostMapping("/member/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute Member member) {
        member.setUpdatedAt();

        member.setArchivedAt(this.memberRepository.findById(id).orElse(null).getArchivedAt());

        this.memberRepository.save(member);

        return "redirect:/member/" + id;
    }

    @GetMapping("/member/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = this.memberRepository.findById(id).orElse(null);

        model.addAttribute("member", member);

        return "member/edit";
    }

    @GetMapping("/member-create")
    public String showForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("member", new Member());
        System.out.println(userDetails.getAuthorities().toString());
        return "member/create";
    }

    @PostMapping("/member-create")
    public String submitForm(Model model, @ModelAttribute Member member) {
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));

        Optional<Role> role = this.roleRepository.findByName(RoleName.ROLE_MEMBER);

        member.setRole(role.get());

        this.memberRepository.save(member);

        return "redirect:/member";
    }

    @GetMapping("/member/{id}/archive")
    public String archiveItem(@PathVariable Long id) {
        Member member = this.memberRepository.findById(id).orElse(null);

        member.setArchivedAt(new Date());
        member.setUpdatedAt();

        this.memberRepository.save(member);

        return "redirect:/member";
    }

    @GetMapping("/member/{id}/unarchive")
    public String unarchiveItem(@PathVariable Long id) {
        Member member = this.memberRepository.findById(id).orElse(null);

        member.setArchivedAt(null);
        member.setUpdatedAt();

        this.memberRepository.save(member);

        return "redirect:/member";
    }
}