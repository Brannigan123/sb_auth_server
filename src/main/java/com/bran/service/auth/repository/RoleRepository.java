package com.bran.service.auth.repository;

import com.bran.service.auth.model.database.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
