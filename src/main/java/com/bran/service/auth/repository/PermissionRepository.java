package com.bran.service.auth.repository;

import com.bran.service.auth.model.database.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
