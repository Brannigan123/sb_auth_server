package com.bran.service.auth.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsUpdateRequest {
    private String otpId;
    private String otpCode;
    private String displayName;
    private String email;
    private String avatarUrl;
    private String password;
}
