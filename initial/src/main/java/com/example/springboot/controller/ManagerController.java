package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import com.example.springboot.repository.ManagerRepository;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.service.ManagerService;
import com.example.springboot.factory.ManagerFactory;
import com.example.springboot.model.Manager;
import java.util.Date;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;

/**
 * Handles all web (HTTP) requests related to Manager users.
 * This includes displaying lists, handling creation, editing, and archiving.
 */

@Controller
public class ManagerController {

    private ManagerService managerService;

    private ManagerRepository managerRepository;

    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Dependency Injection
    public ManagerController(ManagerRepository managerRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, ManagerService managerService) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.managerService = managerService;
    }
    /**
     * Displays a list of managers.
     * It can show 'active' (default) or 'archived' managers based on the 'archived' URL parameter.
     */
    @GetMapping("/manager")
    public String index(Model model, @RequestParam(defaultValue = "0") int archived) {
        // This is the business logic for filtering.
        // This logic should ideally be in the 'ManagerService'.
        model.addAttribute("managers",
                0 == archived ? this.managerService.getActiveManagers() : this.managerService.getArchivedManagers());

        return "manager/list";
    }

    /**
     * Displays the details page for a single manager, found by their ID.
     */
    @GetMapping("/manager/{id}")
    public String show(@PathVariable Long id, Model model) {
        Manager manager = this.managerRepository.findById(id).orElse(null);

        model.addAttribute("manager", manager);

        return "manager/view";
    }

    /**
     * Processes the form submission for updating an existing manager.
     */
    @PostMapping("/manager/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute Manager manager) {
        manager.setUpdatedAt();

        // Preserves the original archivedAt date
        manager.setArchivedAt(this.managerRepository.findById(id).orElse(null).getArchivedAt());

        this.managerRepository.save(manager);

        return "redirect:/manager/" + id;
    }

    /**
     * Displays the form to edit an existing manager's details.
     */
    @GetMapping("/manager/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Manager manager = this.managerRepository.findById(id).orElse(null);

        model.addAttribute("manager", manager);

        return "manager/edit";
    }

    /**
     * Displays the form to create a new manager.
     * Uses the ManagerFactory to create a new, empty Manager object.
     */
    @GetMapping("/manager-create")
    public String showForm(Model model) {
        model.addAttribute("manager", new ManagerFactory().getInstance());

        return "manager/create";
    }

    /**
     * Processes the form submission for creating a new manager.
     * This method also encodes the password and assigns the 'ROLE_MANAGER'.
     */
    @PostMapping("/manager-create")
    public String submitForm(Model model, @ModelAttribute Manager manager) {
        manager.setPassword(this.passwordEncoder.encode(manager.getPassword()));

        Optional<Role> role = this.roleRepository.findByName(RoleName.ROLE_MANAGER);

        manager.setRole(role.get());

        this.managerRepository.save(manager);

        return "redirect:/manager";
    }

    /**
     * Archives a manager (soft delete) by setting their 'archivedAt' date.
     */
    @GetMapping("/manager/{id}/archive")
    public String archiveItem(@PathVariable Long id) {
        Manager manager = this.managerRepository.findById(id).orElse(null);

        manager.setArchivedAt(new Date());
        manager.setUpdatedAt();

        this.managerRepository.save(manager);

        return "redirect:/manager";
    }

    /**
     * Un-archives a manager by setting their 'archivedAt' date back to null.
     */
    @GetMapping("/manager/{id}/unarchive")
    public String unarchiveItem(@PathVariable Long id) {
        Manager manager = this.managerRepository.findById(id).orElse(null);

        manager.setArchivedAt(null);
        manager.setUpdatedAt();

        this.managerRepository.save(manager);

        return "redirect:/manager";
    }
}