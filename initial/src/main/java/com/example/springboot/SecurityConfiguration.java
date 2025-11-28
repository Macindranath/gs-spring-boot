package com.example.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.springboot.authentication.CustomAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Security configuration class for Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        // Custom authentication provider for handling authentication
        @Autowired
        private CustomAuthenticationProvider customAuthenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authenticationProvider(customAuthenticationProvider)
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers(
                                                                "/manager/**",
                                                                "/member/**",
                                                                "/booking")
                                                .hasRole("MANAGER")
                                                .requestMatchers("/").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .permitAll())
                                .logout((logout) -> logout.permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
