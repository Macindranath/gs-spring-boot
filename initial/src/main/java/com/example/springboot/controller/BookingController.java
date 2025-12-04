package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.example.springboot.repository.BookingRepository;
import com.example.springboot.repository.CourtRepository;
import com.example.springboot.repository.MemberRepository;
import com.example.springboot.model.Booking;
import com.example.springboot.model.Court;
import com.example.springboot.model.Member;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;
import java.lang.Iterable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

// Controller to handle booking-related endpoints
@Controller
public class BookingController {

    private BookingRepository bookingRepository;
    private CourtRepository courtRepository;
    private MemberRepository memberRepository;

    public BookingController(
            BookingRepository bookingRepository,
            CourtRepository courtRepository,
            MemberRepository memberRepository) {
        this.bookingRepository = bookingRepository;
        this.courtRepository = courtRepository;
        this.memberRepository = memberRepository;
    }

    // Display a list of bookings, filtered by archived status
    @GetMapping("/booking")
    public String index(Model model, @RequestParam(defaultValue = "0") int archived) {
        List<Booking> result = new ArrayList<Booking>();

        Iterable<Booking> bookings = this.bookingRepository.findAll();

        for (Booking booking : bookings) {
            if (1 == archived) {
                if (null != booking.getArchivedAt()) {
                    result.add(booking);
                }
            } else {
                if (null == booking.getArchivedAt()) {
                    result.add(booking);
                }
            }
        }

        model.addAttribute("bookings", result);

        return "booking/list";
    }

    // Display details of a specific booking by ID
    @GetMapping("/booking/{id}")
    public String show(@PathVariable Long id, Model model) {
        Booking booking = this.bookingRepository.findById(id).orElse(null);

        model.addAttribute("booking", booking);

        return "booking/view";
    }

    // Display the booking creation form
    @GetMapping("/booking-create")
    public String showForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Booking booking = new Booking();

        Optional<Member> currentMember = this.memberRepository.findByEmail(userDetails.getUsername());

        model.addAttribute("bookingError", "");
        model.addAttribute("booking", booking);

        // Prepare the list of courts for the booking form
        Iterable<Court> courts = this.courtRepository.findAll();
        List<Court> selectedCourts = new ArrayList<Court>();
        for (Court court : courts) {
            if (null == court.getArchivedAt()) {
                selectedCourts.add(court);
            }
        }
        model.addAttribute("courts", selectedCourts);

        // Prepare the list of members for the booking form
        Iterable<Member> members = this.memberRepository.findAll();
        List<Member> selectedMembers = new ArrayList<Member>();

        try {
            selectedMembers.add(currentMember.get());
            booking.setMember(currentMember.get());
        } catch (NoSuchElementException e) {
            for (Member member : members) {
                if (null == member.getArchivedAt()) {
                    selectedMembers.add(member);
                }
            }
        }

        model.addAttribute("members", selectedMembers);

        return "booking/create";
    }

    // Handle the submission of the booking creation form
    @PostMapping("/booking-create")
    public String submitForm(
            Model model,
            @ModelAttribute Booking booking,
            @RequestParam("courtId") Long courtId,
            @RequestParam("memberId") Long memberId,
            @AuthenticationPrincipal UserDetails userDetails) {
        booking.setCourt(this.courtRepository.findById(courtId).orElse(null));
        Optional<Member> currentMember = this.memberRepository.findByEmail(userDetails.getUsername());

        // Set the member for the booking, prioritizing the current authenticated member
        try {
            booking.setMember(this.memberRepository.findById(currentMember.get().getId()).orElse(null));
        } catch (NoSuchElementException e) {
            booking.setMember(this.memberRepository.findById(memberId).orElse(null));
        }

        // Retrieve all bookings and filter by court ID
        Iterable<Booking> allBookings = this.bookingRepository.findAll();
        List<Booking> currentBookings = new ArrayList<Booking>();
        for (Booking existingBooking : allBookings) {
            if (courtId == existingBooking.getCourt().getId()) {
                currentBookings.add(existingBooking);
            }
        }

        // Check for booking conflicts with existing bookings
        for (Booking existingBooking : currentBookings) {
            if (existingBooking.getDate().compareTo(booking.getDate()) == 0
                    && (existingBooking.getStartTime().compareTo(booking.getStartTime()) >= 0
                            && existingBooking.getEndTime().compareTo(booking.getEndTime()) <= 0)
                    && "No".equals(existingBooking.getCancelled())) {

                model.addAttribute("booking", booking);

                Iterable<Court> courts = this.courtRepository.findAll();
                List<Court> selectedCourts = new ArrayList<Court>();
                for (Court court : courts) {
                    if (null == court.getArchivedAt()) {
                        selectedCourts.add(court);
                    }
                }
                model.addAttribute("courts", selectedCourts);

                // Prepare the list of members for the booking form
                Iterable<Member> members = this.memberRepository.findAll();
                List<Member> selectedMembers = new ArrayList<Member>();
                for (Member member : members) {
                    if (null == member.getArchivedAt()) {
                        selectedMembers.add(member);
                    }
                }
                model.addAttribute("members", selectedMembers);

                model.addAttribute(
                        "bookingError",
                        "The playing court already has a booking for this time slot. Please choose any another time.");
                return "booking/create";
            }
        }

        this.bookingRepository.save(booking);

       // After successful booking, redirect based on user role
        try {
            if (null != this.memberRepository.findById(currentMember.get().getId()).orElse(null)) {
                return "redirect:/home";
            }
        } catch (NoSuchElementException e) {
            //
        }

        return "redirect:/booking";
    }

    // Handle the cancellation of a booking
    @GetMapping("/booking/{id}/cancel")
    public String cancelItem(@PathVariable Long id) {
        Booking booking = this.bookingRepository.findById(id).orElse(null);

        booking.setCancelled(true);
        booking.setUpdatedAt();

        this.bookingRepository.save(booking);

        return "redirect:/booking";
    }

}