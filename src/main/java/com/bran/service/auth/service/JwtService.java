package com.bran.service.auth.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.val;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expirationTime;

    /**
     * Extracts the claims from the provided token.
     *
     * @param token the token from which to extract the claims
     * @return an Optional containing the claims if extraction is successful,
     *         otherwise an empty Optional
     */
    public Optional<Claims> extractClaims(String token) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                    .getBody();
            return Optional.ofNullable(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if the token claims are valid.
     *
     * @param claims      the claims object representing the token claims
     * @param userDetails the user details object representing the user
     * @return true if the token claims are valid, false otherwise
     */
    public boolean isTokenClaimsValid(Claims claims, UserDetails userDetails) {
        return claims.getSubject().equals(userDetails.getUsername()) && !claims.getExpiration().before(new Date());
    }

    /**
     * Generates a token for the given user details.
     *
     * @param userDetails the user details used to generate the token
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    /**
     * Generates a token for the given user details and claims.
     *
     * @param userDetails the user details used to generate the token
     * @param claims      the claims to be included in the token
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the secret key used for signing.
     *
     * @return the secret key used for signing
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
