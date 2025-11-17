package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import com.example.springboot.repository.CourtRepository;
import com.example.springboot.model.Court;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.lang.Iterable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CourtController {

    private CourtRepository courtRepository;

    public CourtController(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @GetMapping("/court")
    public String index(Model model, @RequestParam(defaultValue = "0") int archived) {
        List<Court> result = new ArrayList<Court>();

        Iterable<Court> courts = this.courtRepository.findAll();

        for (Court court : courts) {
            if (1 == archived) {
                if (null != court.getArchivedAt()) {
                    result.add(court);
                }
            } else {
                if (null == court.getArchivedAt()) {
                    result.add(court);
                }
            }
        }

        model.addAttribute("courts", result);

        return "court/list";
    }

    @GetMapping("/court/{id}")
    public String show(@PathVariable Long id, Model model) {
        Court court = this.courtRepository.findById(id).orElse(null);

        model.addAttribute("court", court);

        return "court/view";
    }

    @PostMapping("/court/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute Court court) {
        court.setUpdatedAt();

        court.setArchivedAt(this.courtRepository.findById(id).orElse(null).getArchivedAt());

        this.courtRepository.save(court);

        return "redirect:/court/" + id;
    }

    @GetMapping("/court/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Court court = this.courtRepository.findById(id).orElse(null);

        model.addAttribute("court", court);

        return "court/edit";
    }

    @GetMapping("/court-create")
    public String showForm(Model model) {
        model.addAttribute("court", new Court());

        return "court/create";
    }

    @PostMapping("/court-create")
    public String submitForm(Model model, @ModelAttribute Court court) {
        this.courtRepository.save(court);

        return "redirect:/court";
    }

    @GetMapping("/court/{id}/archive")
    public String archiveItem(@PathVariable Long id) {
        Court court = this.courtRepository.findById(id).orElse(null);

        court.setArchivedAt(new Date());
        court.setUpdatedAt();

        this.courtRepository.save(court);

        return "redirect:/court";
    }

    @GetMapping("/court/{id}/unarchive")
    public String unarchiveItem(@PathVariable Long id) {
        Court court = this.courtRepository.findById(id).orElse(null);

        court.setArchivedAt(null);
        court.setUpdatedAt();

        this.courtRepository.save(court);

        return "redirect:/court";
    }
}