package com.bran.service.auth.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.bran.service.auth.model.database.RefreshToken;
import com.bran.service.auth.model.database.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    /**
     * Delete records by the specified user.
     *
     * @param user the user whose records will be deleted
     * @return the number of records deleted
     */
    @Modifying
    int deleteByUser(User user);

    /**
     * Deletes all entries with an expiry date less than the specified date.
     *
     * @param now the current date
     * @return the number of deleted entries
     */
    @Modifying
    int deleteByExpiryDateLessThan(Date now);
}
