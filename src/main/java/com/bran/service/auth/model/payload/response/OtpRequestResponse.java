package com.bran.service.auth.model.payload.response;

import java.util.Date;

import com.bran.service.auth.model.payload.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OtpRequestResponse extends ApiResponse {
    private String otpId;
    private String email;
    private Date expiryDate;
}
