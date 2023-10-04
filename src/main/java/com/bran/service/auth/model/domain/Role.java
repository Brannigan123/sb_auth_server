package com.bran.service.auth.model.domain;

import java.util.stream.Stream;

import lombok.val;

public enum Role {
    USER(), STAFF(), ADMIN(), SUPER_ADMIN(),
    ;

    Permission[] permissions;

    Role(Permission... permissions) {
        this.permissions = permissions;
    }

    public String id() {
        return String.format("ROLE_%s", name());
    }

    public com.bran.service.auth.model.database.Role asNewDbRole() {
        val dbPermissions = Stream
                .of(permissions)
                .map(Permission::asNewDbPermission)
                .toList();
        return new com.bran.service.auth.model.database.Role(id(), name(),
                dbPermissions);
    }
}
