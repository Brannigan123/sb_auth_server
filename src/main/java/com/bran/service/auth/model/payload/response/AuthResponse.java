package com.bran.service.auth.model.payload.response;

import com.bran.service.auth.model.payload.ApiResponse;
import com.bran.service.auth.model.payload.ResponseUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

@With
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthResponse extends ApiResponse {
    private String token;
    private String refreshToken;
    private ResponseUserDetails userDetails;
}
