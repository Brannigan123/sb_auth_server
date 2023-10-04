package com.bran.service.auth.model.database;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bran.service.auth.config.AttributeEncryptor;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "fxcp_user", indexes = { @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true) })
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false, updatable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String username;
    @Column(nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String displayName;
    @Column(unique = true, nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String email;
    @Column(nullable = false)
    private String password;
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();
    @Column(nullable = false)
    private boolean emailVerified;
    @Column(nullable = false)
    private boolean lockedAccount;
    @Column(nullable = false)
    private boolean deletedAccount;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns a list of permissions for this user.
     *
     * @return a list of permissions for this user.
     */
    public List<Permission> getPermissions() {
        return roles.stream().flatMap(role -> role.getPermissions().stream()).toList();
    }

    /**
     * Retrieves the collection of granted authorities for this user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPermissions();
    }

}
