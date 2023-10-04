package com.bran.service.auth.service;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bran.service.auth.model.database.RefreshToken;
import com.bran.service.auth.model.database.User;
import com.bran.service.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    /**
     * Finds a refresh token by token string.
     *
     * @param token the token string to search for
     * @return an optional refresh token, empty if not found
     */
    public Optional<RefreshToken> findByTokenString(String token) {
        return refreshTokenRepository.findByToken(token).flatMap(this::verifyExpiration);
    }

    /**
     * Verifies if the given refresh token has expired.
     *
     * @param token the refresh token to be verified
     * @return an Optional containing the refreshed token if it has not expired, or
     *         an empty Optional if it has expired
     */
    private Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            return Optional.empty();
        }
        return Optional.ofNullable(token);
    }

    /**
     * Creates a refresh token for the given user.
     *
     * @param user the user for whom the refresh token is created
     * @return the created refresh token
     */
    public RefreshToken createRefreshToken(User user) {
        val refreshToken = RefreshToken.builder()
                .token(String.format("%s-%s", user.getId(), RandomStringUtils.randomAlphanumeric(32)))
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Deletes a user's refresh tokens.
     *
     * @param user the user object
     * @return the number of refresh tokens deleted
     */
    @Transactional
    public int deleteByUser(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Deletes a RefreshToken.
     *
     * @param token the RefreshToken to be deleted
     */
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
}
