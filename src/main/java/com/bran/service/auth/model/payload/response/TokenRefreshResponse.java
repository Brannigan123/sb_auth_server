package com.bran.service.auth.model.payload.response;

import java.util.List;

import com.bran.service.auth.model.database.Role;
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
public class TokenRefreshResponse extends ApiResponse {
    private String token;
    private String refreshToken;
    private String username;
    private String displayName;
    private String email;
    private List<Role> roles;
    private boolean emailVerified;
    private boolean lockedAccount;
    private boolean deletedAccount;
}
