package com.bran.service.auth.task;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.bran.service.auth.repository.OtpRepository;
import com.bran.service.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TokenPurgingTask {
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpRepository otpRepository;

    // Dormant accounts that haven't been enabled,
    /**
     * Purges expired refresh tokens.
     *
     * @param None This function does not take any parameters.
     * @return None This function does not return anything.
     */
    @Transactional
    @Scheduled(cron = "${jwt.expired-refresh-token-purging-cron}")
    public void purgeExpiredRefreshTokens() {
        refreshTokenRepository.deleteByExpiryDateLessThan(new Date());
    }

    /**
     * Purges expired OTPs from the system.
     *
     * @param None This function does not take any parameters.
     * @return None This function does not return anything.
     */
    @Transactional
    @Scheduled(cron = "${otp.expired-otp-purging-cron}")
    public void purgeExpiredOTPs() {
        otpRepository.deleteByExpiryDateLessThan(new Date());
    }
}
