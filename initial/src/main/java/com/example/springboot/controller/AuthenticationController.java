package com.example.springboot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @GetMapping("/userinfo")
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "Authenticated Username: " + userDetails.getUsername()
                + "\nRole " + userDetails.getAuthorities().toString();
    }
}
