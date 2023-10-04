package com.bran.service.auth.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bran.service.auth.model.domain.Permission;
import com.bran.service.auth.model.domain.Role;
import com.bran.service.auth.repository.PermissionRepository;
import com.bran.service.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class RolePermissionSetup implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        populateRolesAndPermissionsData();
        alreadySetup = true;
    }

    /**
     * Populates the data by creating permissions and roles if they are not found.
     */
    @Transactional
    public void populateRolesAndPermissionsData() {
        for (val permission : Permission.values()) {
            createPermissionIfNotFound(permission);
        }
        for (val role : Role.values()) {
            createRoleIfNotFound(role);
        }
    }

    /**
     * Creates a new role if it is not found in the role repository.
     *
     * @param role The role to be created.
     * @return The created role if found in the repository, otherwise a new role
     *         saved in the repository.
     */
    @Transactional
    public com.bran.service.auth.model.database.Role createRoleIfNotFound(
            Role role) {
        return roleRepository.findById(role.id())
                .orElseGet(() -> roleRepository
                        .save(role.asNewDbRole()));
    }

    /**
     * Creates a new permission if it is not found in the permission repository.
     *
     * @param permission the permission object to be created
     * @return the created permission object
     */
    @Transactional
    public com.bran.service.auth.model.database.Permission createPermissionIfNotFound(Permission permission) {
        return permissionRepository.findById(permission.id())
                .orElseGet(() -> permissionRepository.save(permission.asNewDbPermission()));
    }

}
