package com.bran.service.auth.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.bran.service.auth.model.database.OTP;
import com.bran.service.auth.model.database.User;

public interface OtpRepository extends JpaRepository<OTP, String> {

    /**
     * Deletes the records associated with the given user.
     *
     * @param user the user whose records will be deleted
     * @return the number of records deleted
     */
    @Modifying
    int deleteByUser(User user);

    /**
     * Deletes entities with an expiry date less than the specified date.
     *
     * @param now the date to compare against
     * @return the number of entities deleted
     */
    @Modifying
    int deleteByExpiryDateLessThan(Date now);
}
