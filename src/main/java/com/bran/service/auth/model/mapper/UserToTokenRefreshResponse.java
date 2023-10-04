package com.bran.service.auth.model.mapper;

import java.util.List;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.TokenRefreshResponse;

public class UserToTokenRefreshResponse {
    private UserToTokenRefreshResponse() {
    }

    /**
     * Maps the given user, JWT, and refresh token to a TokenRefreshResponse object.
     *
     * @param user         the user object to be mapped
     * @param jwt          the JWT token
     * @param refreshToken the refresh token
     * @return the mapped TokenRefreshResponse object
     */
    public static TokenRefreshResponse map(final User user, final String jwt, final String refreshToken) {
        return TokenRefreshResponse.builder().token(jwt).refreshToken(refreshToken)
                .username(user.getUsername()).displayName(user.getDisplayName())
                .email(user.getEmail()).roles(user.getRoles()).emailVerified(user.isEmailVerified())
                .lockedAccount(user.isLockedAccount())
                .deletedAccount(user.isDeletedAccount()).errored(false).messages(List.of("Token refreshed."))
                .build();
    }
}
