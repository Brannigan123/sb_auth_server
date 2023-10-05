package com.bran.service.auth.model.payload;

import java.util.List;

import com.bran.service.auth.model.database.Role;

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
public class ResponseUserDetails {
    private String userId;
    private String username;
    private String displayName;
    private String email;
    private List<Role> roles;
    private boolean emailVerified;
    private boolean lockedAccount;
    private boolean deletedAccount;
}
