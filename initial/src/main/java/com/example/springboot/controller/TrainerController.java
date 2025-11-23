package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import com.example.springboot.repository.TrainerRepository;
import com.example.springboot.model.Trainer;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.lang.Iterable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Handles all web (HTTP) requests related to Trainer users.
 * This includes displaying lists, handling creation, editing, and archiving.
 */
@Controller
public class TrainerController {

    private TrainerRepository trainerRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public TrainerController(TrainerRepository trainerRepository, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Displays a list of trainers.
     * It can show 'active' (default) or 'archived' trainers based on the 'archived' URL parameter.
     */
    @GetMapping("/trainer")
    public String index(Model model, @RequestParam(defaultValue = "0") int archived) {
        // This is the business logic for filtering.
        List<Trainer> result = new ArrayList<Trainer>();

        Iterable<Trainer> trainers = this.trainerRepository.findAll();

        for (Trainer trainer : trainers) {
            if (1 == archived) {
                if (null != trainer.getArchivedAt()) {
                    result.add(trainer);
                }
            } else {
                if (null == trainer.getArchivedAt()) {
                    result.add(trainer);
                }
            }
        }

        model.addAttribute("trainers", result);

        return "trainer/list";
    }

    /**
     * Displays the details page for a single trainer, found by their ID.
     */
    @GetMapping("/trainer/{id}")
    public String show(@PathVariable Long id, Model model) {
        Trainer trainer = this.trainerRepository.findById(id).orElse(null);

        model.addAttribute("trainer", trainer);

        return "trainer/view";
    }

    /**
     * Processes the form submission for updating an existing trainer.
     */
    @PostMapping("/trainer/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute Trainer trainer) {
        trainer.setUpdatedAt();

        // Preserves the original archivedAt date
        trainer.setArchivedAt(this.trainerRepository.findById(id).orElse(null).getArchivedAt());

        this.trainerRepository.save(trainer);

        return "redirect:/trainer/" + id;
    }

    /**
     * Displays the form to edit an existing trainer's details.
     */
    @GetMapping("/trainer/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Trainer trainer = this.trainerRepository.findById(id).orElse(null);

        model.addAttribute("trainer", trainer);

        return "trainer/edit";
    }

    /**
     * Displays the form to create a new trainer.
     */
    @GetMapping("/trainer-create")
    public String showForm(Model model) {
        model.addAttribute("trainer", new Trainer());

        return "trainer/create";
    }

    /**
     * Processes the form submission for creating a new trainer.
     * This one is missing password encoding and role assignment
     */
    @PostMapping("/trainer-create")
    public String submitForm(Model model, @ModelAttribute Trainer trainer) {
        this.trainerRepository.save(trainer);

        return "redirect:/trainer";
    }

    
    @GetMapping("/trainer/{id}/archive")
    public String archiveItem(@PathVariable Long id) {
        Trainer trainer = this.trainerRepository.findById(id).orElse(null);

        trainer.setArchivedAt(new Date());
        trainer.setUpdatedAt();

        this.trainerRepository.save(trainer);

        return "redirect:/trainer";
    }

    /**
     * Un-archives a trainer by setting their 'archivedAt' date back to null.
     */
    @GetMapping("/trainer/{id}/unarchive")
    public String unarchiveItem(@PathVariable Long id) {
        Trainer trainer = this.trainerRepository.findById(id).orElse(null);

        trainer.setArchivedAt(null);
        trainer.setUpdatedAt();

        this.trainerRepository.save(trainer);

        return "redirect:/trainer";
    }
}