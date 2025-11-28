package com.example.springboot.authentication;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Custom authentication provider that handles user authentication.
 * It checks for a hardcoded administrator user and delegates other authentication
 * requests to the CustomUserDetailsService.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // Dependency Injection for the password encoder and user details service
    //Lazy to avoid circular dependencies
    //autowired to let Spring inject the dependencies
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private CustomUserDetailsService userDetailsService;

    //override the authenticate method to provide custom authentication logic
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if ("administrator".equals(username) && "passwordpassword".equals(password)) {
            System.out.println("Authenticated");
            return new UsernamePasswordAuthenticationToken(
                    User.builder().username(username).password(password).roles("ADMINISTRATOR")
                            .build(),
                    password);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
    
    //override the supports method to specify the type of authentication supported
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}