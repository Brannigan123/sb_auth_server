package com.bran.service.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bran.service.auth.service.SpringUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final SpringUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Creates a SecurityFilterChain for handling security configurations in the
     * HTTP requests.
     *
     * @param httpSecurity the HttpSecurity object used for configuring security
     *                     settings
     * @return the SecurityFilterChain object representing the security filter chain
     * @throws Exception if an error occurs while configuring the security filter
     *                   chain
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/v1/auth/public/**", "/v3/api-docs/**", "/swagger-ui.html",
                                        "/swagger-ui/**")
                                .permitAll().anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Creates and configures an AuthenticationProvider for user authentication.
     *
     * @return the configured AuthenticationProvider
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        val authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * Creates an authentication manager using the provided authentication
     * configuration.
     *
     * @param configuration the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs while creating the authentication
     *                   manager
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Returns a new instance of the PasswordEncoder interface, which is implemented
     * by the BCryptPasswordEncoder class. The BCryptPasswordEncoder class is a
     * password encoder that uses the BCrypt hashing algorithm with a work factor
     * of 13. The work factor determines the computational cost of hashing the
     * password and can be adjusted to make the hashing process more or less
     * computationally expensive.
     *
     * @return a new instance of the PasswordEncoder interface
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
