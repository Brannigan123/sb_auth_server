package com.bran.service.auth.model.domain;

public enum Permission {
    ;
    public String id() {
        return String.format("PERMISSION_%s", name());
    }

    public com.bran.service.auth.model.database.Permission asNewDbPermission() {
        return new com.bran.service.auth.model.database.Permission(id(),
                name(), null);
    }
}
