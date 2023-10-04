package com.bran.service.auth.model.mapper;

import java.util.List;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.SigninResponse;

public class UserToSigninResponse {
    private UserToSigninResponse() {
    }

    /**
     * Maps the given user object, JWT, and refresh token to a SigninResponse
     * object.
     *
     * @param user         the user object to be mapped
     * @param jwt          the JSON Web Token
     * @param refreshToken the refresh token
     * @return the SigninResponse object with the mapped values
     */
    public static SigninResponse map(final User user, final String jwt, final String refreshToken) {
        return SigninResponse.builder().token(jwt).refreshToken(refreshToken)
                .username(user.getUsername()).displayName(user.getDisplayName())
                .email(user.getEmail()).roles(user.getRoles())
                .emailVerified(user.isEmailVerified()).lockedAccount(user.isLockedAccount())
                .deletedAccount(user.isDeletedAccount()).errored(false)
                .messages(List.of("Login successful.")).build();
    }
}
