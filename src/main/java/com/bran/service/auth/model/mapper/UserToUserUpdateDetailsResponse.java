package com.bran.service.auth.model.mapper;

import java.util.List;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.UserDetailsUpdateResponse;

public class UserToUserUpdateDetailsResponse {
    private UserToUserUpdateDetailsResponse() {
    }

    /**
     * Maps the given user, JWT, and refresh token to a UserDetailsUpdateResponse
     * object.
     *
     * @param user         the user object to be mapped
     * @param jwt          the JWT token
     * @param refreshToken the refresh token
     * @return the UserDetailsUpdateResponse object with the mapped values
     */
    public static UserDetailsUpdateResponse map(final User user, final String jwt, final String refreshToken) {
        return UserDetailsUpdateResponse.builder().token(jwt)
                .refreshToken(refreshToken)
                .username(user.getUsername()).displayName(user.getDisplayName())
                .email(user.getEmail()).roles(user.getRoles())
                .emailVerified(user.isEmailVerified())
                .lockedAccount(user.isLockedAccount())
                .deletedAccount(user.isDeletedAccount()).errored(false)
                .messages(List.of("User details updated."))
                .build();
    }
}
