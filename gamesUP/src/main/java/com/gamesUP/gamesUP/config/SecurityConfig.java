package com.gamesUP.gamesUP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Endpoints publics (pas d'authentification requise)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/clients").permitAll()
                .requestMatchers("/api/users/admins").permitAll()
                .requestMatchers("/api/games").permitAll()
                .requestMatchers("/api/games/search").permitAll()
                .requestMatchers("/api/games/{id}").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/publishers/**").permitAll()
                .requestMatchers("/api/authors/**").permitAll()
                .requestMatchers("/api/recommendations/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // Endpoints nécessitant une authentification
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/purchases/**").authenticated()
                .requestMatchers("/api/avis/**").authenticated()
                .requestMatchers("/api/wishlist/**").authenticated()
                .requestMatchers("/api/games/**").authenticated() // Pour POST, PUT, DELETE
                
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable()); // Pour H2 Console
        
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();
        
        UserDetails client = User.builder()
            .username("client")
            .password(passwordEncoder().encode("client"))
            .roles("CLIENT")
            .build();
        
        return new InMemoryUserDetailsManager(admin, client);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 