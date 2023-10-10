package com.bran.service.auth.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bran.service.auth.model.database.OTP;
import com.bran.service.auth.model.database.User;
import com.bran.service.auth.repository.OtpRepository;
import com.bran.service.auth.template.OtpVerifyTemplate;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender javaMailSender;

    @Value("${otp.length}")
    private Integer otpLength;
    @Value("${otp.expiration}")
    private Long otpExpiration;

    /**
     * Finds the OTP (One-Time Password) by the given User and code.
     *
     * @param user the User object to search for
     * @param code the code to search for
     * @return an Optional containing the OTP if found, or an empty Optional if not
     *         found
     */
    public Optional<OTP> findById(String id) {
        return otpRepository.findById(id).flatMap(this::verifyExpiration);
    }

    /**
     * Verifies if the given OTP token has expired.
     *
     * @param token the OTP token to be verified
     * @return an optional containing the token if it has not expired, or an empty
     *         optional if it has expired
     */
    private Optional<OTP> verifyExpiration(OTP token) {
        if (token.getExpiryDate().before(new Date())) {
            otpRepository.delete(token);
            return Optional.empty();
        }
        return Optional.ofNullable(token);
    }

    /**
     * Generates a One-Time Password (OTP) for the given user.
     *
     * @param user the user for whom the OTP is generated
     * @return the generated OTP
     */
    public OTP createOTP(User user) {
        val numbers = "0123456789";
        val rng = new SecureRandom();
        char[] code = new char[otpLength];
        for (int i = 0; i < otpLength; i++) {
            code[i] = numbers.charAt(rng.nextInt(numbers.length()));
        }
        val otpString = new String(code);
        val otp = OTP.builder().user(user).code(otpString)
                .expiryDate(new Date(System.currentTimeMillis() + otpExpiration + Duration.ofMinutes(1).toMillis()))
                .build();
        return otpRepository.save(otp);
    }

    public void sendOTPEmail(User user, OTP otp, String purpose)
            throws UnsupportedEncodingException, MessagingException {
        val message = javaMailSender.createMimeMessage();
        val helper = new MimeMessageHelper(message);

        helper.setFrom("brannigansakwah@gmail.com", "Bran from gmail 😜");
        helper.setTo(user.getEmail());

        val minutes = Duration.between(new Date().toInstant(), otp.getExpiryDate().toInstant()).toMinutes();
        val subject = String.format("Here's your One Time Password (OTP) - Expire in %d minutes!", minutes);
        helper.setSubject(subject);

        val body = OtpVerifyTemplate.buildHtml("https://mailmeteor.com/assets/img/brand/icon.svg", purpose,
                otp.getCode());

        helper.setText(body, true);

        javaMailSender.send(message);
    }

    /**
     * Deletes the records associated with a user.
     *
     * @param user the user whose records will be deleted
     * @return the number of records deleted
     */
    @Transactional
    public int deleteByUser(User user) {
        return otpRepository.deleteByUser(user);
    }

    /**
     * Deletes the specified OTP.
     *
     * @param otp the OTP to be deleted
     */
    public void delete(OTP otp) {
        otpRepository.delete(otp);
    }
}
