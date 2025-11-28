package com.example.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springboot.model.Booking;
import com.example.springboot.model.Member;
import com.example.springboot.repository.BookingRepository;
import com.example.springboot.repository.MemberRepository;

// Home controller to handle the home page and user-specific data
@Controller
public class HomeController {

	private MemberRepository memberRepository;
	private BookingRepository bookingRepository;

	public HomeController(
			MemberRepository memberRepository,
			BookingRepository bookingRepository) {
		this.memberRepository = memberRepository;
		this.bookingRepository = bookingRepository;
	}

	// Redirect root URL to the home page
	@GetMapping("/")
	public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		return "redirect:/home";
	}

	// Display the home page with user-specific bookings
	@GetMapping("/home")
	public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Optional<Member> currentMember = this.memberRepository.findByEmail(userDetails.getUsername());

		// Retrieve and filter bookings for the current member
		try {
			Iterable<Booking> allBookings = this.bookingRepository.findAll();
			List<Booking> myBookings = new ArrayList<Booking>();
			for (Booking booking : allBookings) {
				if (null == booking.getArchivedAt() && null != booking.getMember()
						&& (booking.getMember().getId() == currentMember.get().getId())) {
					myBookings.add(booking);
				}
			}

			System.out.println(myBookings.size());
			model.addAttribute("myBookings", myBookings);
		} catch (NoSuchElementException e) {
		}

		return "home";
	}

}
