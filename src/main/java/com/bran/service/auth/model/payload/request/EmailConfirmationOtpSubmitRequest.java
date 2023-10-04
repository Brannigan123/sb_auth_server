package com.bran.service.auth.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfirmationOtpSubmitRequest {
    private String otpId;
    private String code;
}