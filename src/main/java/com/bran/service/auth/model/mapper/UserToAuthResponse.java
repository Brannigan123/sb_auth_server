package com.bran.service.auth.model.mapper;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.AuthResponse;

public class UserToAuthResponse {
    private UserToAuthResponse() {
    }

    /**
     * Maps the given user, JWT, and refresh token to an AuthResponse
     * object.
     *
     * @param user         the user object to be mapped
     * @param jwt          the JWT token
     * @param refreshToken the refresh token
     * @return the UserDetailsUpdateResponse object with the mapped values
     */
    public static AuthResponse map(final User user, final String jwt, final String refreshToken) {
        return AuthResponse.builder().token(jwt)
                .refreshToken(refreshToken).userDetails(UserToResponseUserDetails.map(user)).errored(false)
                .build();
    }
}
