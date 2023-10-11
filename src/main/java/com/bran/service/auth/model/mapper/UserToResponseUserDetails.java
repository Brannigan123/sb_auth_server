package com.bran.service.auth.model.mapper;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.ResponseUserDetails;

public class UserToResponseUserDetails {
    private UserToResponseUserDetails() {
    }

    /**
     * Maps a User object to a ResponseUserDetails object.
     *
     * @param user the User object to be mapped
     * @return the mapped ResponseUserDetails object
     */
    public static ResponseUserDetails map(final User user) {
        return ResponseUserDetails.builder().userId(user.getId())
                .username(user.getUsername()).displayName(user.getDisplayName())
                .email(user.getEmail()).roles(user.getRoles())
                .avatarUrl(user.getAvatarUrl())
                .emailVerified(user.isEmailVerified())
                .lockedAccount(user.isLockedAccount())
                .deletedAccount(user.isDeletedAccount())
                .build();
    }
}
