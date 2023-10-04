package com.bran.service.auth.model.mapper;

import java.util.List;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.SignupResponse;

public class UserToSignupResponse {
    private UserToSignupResponse() {
    }

    /**
     * Maps the given user, JWT, and refresh token to a SignupResponse object.
     *
     * @param user         the user object to map
     * @param jwt          the JWT token
     * @param refreshToken the refresh token
     * @return the SignupResponse object
     */
    public static SignupResponse map(final User user, final String jwt, final String refreshToken) {
        return SignupResponse.builder().token(jwt).refreshToken(refreshToken)
                .username(user.getUsername()).displayName(user.getDisplayName())
                .email(user.getEmail()).roles(user.getRoles())
                .emailVerified(user.isEmailVerified()).lockedAccount(user.isLockedAccount())
                .deletedAccount(user.isDeletedAccount()).errored(false)
                .messages(List.of("Your account has been created.")).build();
    }
}
