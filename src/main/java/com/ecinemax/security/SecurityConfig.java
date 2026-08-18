package com.ecinemax.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    // BCrypt: a slow, salted hashing algorithm designed specifically for
    // passwords. We never store or compare plaintext passwords anywhere.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Wires AppUserDetailsService + the password encoder together so login
    // attempts get checked against the database correctly.
    @Bean
    public AuthenticationManager authenticationManager(AppUserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new org.springframework.security.authentication.ProviderManager(provider);
    }

    // Where the logged-in user's identity gets stored between requests - in
    // the HTTP session. AuthController saves into this explicitly on login.
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                // This app has no separate frontend origin - the browser only ever
                // talks to this same server, and every state-changing endpoint is a
                // JSON API (not a traditional HTML <form> post). CSRF token wiring
                // for a plain-JS frontend adds real complexity for limited benefit
                // here; a production deployment should revisit this.
                .csrf(AbstractHttpConfigurer::disable)
                .securityContext(context -> context.securityContextRepository(securityContextRepository))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // We built our own JSON login endpoint (AuthController), so we don't
                // want Spring Security's default HTML login page or basic-auth popup.
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Without this, Spring Security's default for an unauthenticated
                // request to a protected endpoint is 403 Forbidden. 401
                // Unauthorized is the more correct response here: it means "we
                // don't know who you are", as opposed to 403's "we know who you
                // are, but you're not allowed".
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/movies/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        // Everything else (the static HTML/CSS/JS/images) is open at the
                        // HTTP level - pages decide what to show based on /api/auth/me,
                        // but the real access control lives on the API above.
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
