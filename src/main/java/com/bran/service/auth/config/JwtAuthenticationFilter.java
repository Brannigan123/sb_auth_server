package com.bran.service.auth.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.service.JwtService;
import com.bran.service.auth.service.SpringUserDetailsService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;
    private final SpringUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        val jwt = authorizationHeader.substring(BEARER.length());
        val claims = jwtService.extractClaims(jwt);
        if (claims.isPresent() && isNotAuthenticated()) {
            authenicateTokenClaims(request, claims.get());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the user is not authenticated.
     *
     * @return true if the user is not authenticated, false otherwise
     */
    private boolean isNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    /**
     * Authenticates the token claims using the provided HttpServletRequest and
     * Claims objects.
     *
     * @param request the HttpServletRequest object containing the request
     *                information
     * @param claims  the Claims object containing the token claims
     */
    private void authenicateTokenClaims(HttpServletRequest request, Claims claims) {
        try {
            val user = userDetailsService
                    .loadUserByUsername(claims.getSubject());
            if (jwtService.isTokenClaimsValid(claims, user)) {
                updateSecurityContext(request, user);
            }
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * Updates the security context with the given user details.
     *
     * @param request     the HTTP servlet request
     * @param userDetails the user details
     */
    private void updateSecurityContext(HttpServletRequest request, final User user) {
        val authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getId(), null, user.getPermissions());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
