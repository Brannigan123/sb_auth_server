package com.bran.service.auth.model.mapper;

import java.util.List;

import com.bran.service.auth.model.database.OTP;
import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.payload.response.OtpRequestResponse;

public class OtpToOtpResponse {
    private OtpToOtpResponse() {
    }

    /**
     * Maps a User and OTP to an OtpRequestResponse.
     *
     * @param user the User object to map
     * @param otp  the OTP object to map
     * @return the mapped OtpRequestResponse object
     */
    public static OtpRequestResponse map(final User user, final OTP otp) {
        return OtpRequestResponse.builder().errored(false)
                .otpId(otp.getId())
                .email(user.getEmail())
                .expiryDate(otp.getExpiryDate())
                .messages(List.of("Verification email sent."))
                .build();
    }
}
