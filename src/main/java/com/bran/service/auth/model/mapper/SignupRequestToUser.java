package com.bran.service.auth.model.mapper;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.domain.Role;
import com.bran.service.auth.model.payload.request.SignupRequest;

public class SignupRequestToUser {
    private SignupRequestToUser() {
    }

    /**
     * Maps a SignupRequest object to a User object.
     *
     * @param request         the SignupRequest object to be mapped
     * @param passwordEncoder the PasswordEncoder used to encode the password
     * @return the mapped User object
     */
    public static User map(final SignupRequest request, final PasswordEncoder passwordEncoder) {
        return User.builder().username(request.getUsername())
                .displayName(request.getDisplayName() == null || request.getDisplayName().isBlank()
                        ? request.getUsername()
                        : request.getDisplayName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).roles(List.of(Role.USER.asNewDbRole()))
                .emailVerified(false).lockedAccount(false).deletedAccount(false).build();
    }
}
