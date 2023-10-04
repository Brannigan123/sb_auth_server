package com.bran.service.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bran.service.auth.model.database.User;

public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their username.
     * 
     * @param username the username of the user
     * @return an optional containing the user if found, otherwise empty
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return an optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their email or username.
     *
     * @param usernameOrEmail the email or username of the user
     * @return an optional containing the user if found, otherwise empty
     */
    @Query("select u from User u where u.email = ?1 OR u.username = ?1")
    Optional<User> findByEmailOrUsername(String usernameOrEmail);
}
