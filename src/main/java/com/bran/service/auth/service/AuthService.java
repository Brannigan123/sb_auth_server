package com.bran.service.auth.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bran.service.auth.model.database.User;
import com.bran.service.auth.model.mapper.OtpToOtpResponse;
import com.bran.service.auth.model.mapper.SignupRequestToUser;
import com.bran.service.auth.model.mapper.UserToAuthResponse;
import com.bran.service.auth.model.mapper.UserToResponseUserDetails;
import com.bran.service.auth.model.payload.request.EmailConfirmationOtpSubmitRequest;
import com.bran.service.auth.model.payload.request.OtpRequest;
import com.bran.service.auth.model.payload.request.ResetUserPasswordRequest;
import com.bran.service.auth.model.payload.request.SigninRequest;
import com.bran.service.auth.model.payload.request.SignoutRequest;
import com.bran.service.auth.model.payload.request.SignupRequest;
import com.bran.service.auth.model.payload.request.TokenRefreshRequest;
import com.bran.service.auth.model.payload.request.UserDetailsUpdateRequest;
import com.bran.service.auth.model.payload.response.OtpRequestResponse;
import com.bran.service.auth.model.payload.response.AuthResponse;
import com.bran.service.auth.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String YOU_ARE_NOT_SIGNED_IN_PLEASE_SIGN_IN_AND_TRY_AGAIN = "You are not signed in. Please sign in and try again.";
    private static final String INVALID_OTP_OR_EXPIRED_PLEASE_TRY_AGAIN = "Invalid or expired OTP. Please try again";
    private static final String YOU_ARE_NOT_SIGNED_IN = "You are not signed in.";

    private static final Pattern EMAIL_REGEX_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request the registration request containing user information
     * @return the registration response with a token if successful, or an error
     *         response if there are validation errors
     */
    public AuthResponse register(SignupRequest request) {
        val errors = checkRegistrationRequest(request, new ArrayList<>());
        if (errors.isEmpty()) {
            val user = SignupRequestToUser.map(request, passwordEncoder);
            val createdUser = userRepository.save(user);
            val jwtAndRefreshToken = createJwtAndrefreshTokens(createdUser);
            return UserToAuthResponse.map(createdUser, jwtAndRefreshToken.getLeft(), jwtAndRefreshToken.getRight());
        }
        return AuthResponse.builder().errored(true).messages(errors).build();
    }

    /**
     * Authenticates the user based on the provided SigninRequest.
     *
     * @param request the SigninRequest object containing the user's credentials
     * @return the SigninResponse object containing the authentication result
     */
    public AuthResponse authenticate(SigninRequest request) {
        val errors = checkLoginRequest(request, new ArrayList<>());
        if (errors.isEmpty()) {
            val user = userRepository.findByEmailOrUsername(request.getEmailOrUsername());
            if (user.isPresent()) {
                val matchedUser = user.get();
                try {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(matchedUser.getId(),
                                    request.getPassword()));
                    val jwtAndRefreshToken = createJwtAndrefreshTokens(matchedUser);
                    return UserToAuthResponse.map(matchedUser, jwtAndRefreshToken.getLeft(),
                            jwtAndRefreshToken.getRight());
                } catch (Exception e) {
                    errors.add("Invalid credentials. Please try again or reset your password.");
                }
            } else {
                errors.add("Invalid credentials. Please try again or reset your password.");
            }
        }
        return AuthResponse.builder().errored(true).messages(errors).build();
    }

    /**
     * Refreshes the token and returns the response.
     *
     * @param request the token refresh request
     * @return the token refresh response
     */
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        val errors = checkRefreshTokenRequest(request, new ArrayList<>());
        if (errors.isEmpty()) {
            val token = refreshTokenService.findByTokenString(request.getToken());
            if (token.isPresent()) {
                val user = token.get().getUser();
                val jwtAndRefreshToken = createJwtAndrefreshTokens(user);
                refreshTokenService.delete(token.get());
                return UserToAuthResponse.map(user, jwtAndRefreshToken.getLeft(),
                        jwtAndRefreshToken.getRight());
            } else {
                errors.add("Your session has expired. Please sign in again.");
            }
        }
        return AuthResponse.builder().errored(true).messages(errors).build();
    }

    /**
     * Sign out the user.
     *
     * @param request the signout request
     * @return the API response indicating the success or failure of the signout
     *         operation
     */
    public AuthResponse signout(SignoutRequest request) {
        val userIdOptional = getLogedinUserId();
        if (userIdOptional.isEmpty()) {
            return AuthResponse.builder().errored(true)
                    .messages(List.of(YOU_ARE_NOT_SIGNED_IN_PLEASE_SIGN_IN_AND_TRY_AGAIN))
                    .build();
        }
        val userId = userIdOptional.get();
        val user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (request.getToken() == null) {
                refreshTokenService.deleteByUser(user.get());
                return AuthResponse.builder().errored(false)
                        .messages(List.of("You have been signed out from all your devices."))
                        .build().withUserDetails(UserToResponseUserDetails.map(user.get()));
            } else {
                val token = refreshTokenService.findByTokenString(request.getToken());
                if (token.isPresent()) {
                    refreshTokenService.delete(token.get());
                    return AuthResponse.builder().errored(false)
                            .messages(List.of("You have been signed out."))
                            .build().withUserDetails(UserToResponseUserDetails.map(user.get()));
                }
            }
        }
        return AuthResponse.builder().errored(true).messages(List.of(YOU_ARE_NOT_SIGNED_IN)).build();
    }

    /**
     * Sends an email verification email to the logged-in user.
     *
     * @return an OtpRequestResponse object representing the result of sending the
     *         email verification email
     */
    public OtpRequestResponse sendEmailVerificationEmail() {
        val userIdOptional = getLogedinUserId();
        if (userIdOptional.isEmpty()) {
            return OtpRequestResponse.builder().errored(true)
                    .messages(List.of(YOU_ARE_NOT_SIGNED_IN_PLEASE_SIGN_IN_AND_TRY_AGAIN))
                    .build();
        }
        val userId = userIdOptional.get();
        val userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            val user = userOptional.get();
            val otp = otpService.createOTP(user);
            try {
                otpService.sendOTPEmail(user, otp,
                        "Use the code to verify your email.");
                return OtpToOtpResponse.map(user, otp);
            } catch (Exception e) {
                e.printStackTrace();
                return OtpRequestResponse.builder().errored(true)
                        .messages(List.of("Error sending verification email. Please try again later."))
                        .build();
            }
        } else {
            return OtpRequestResponse.builder().errored(true).messages(List.of(YOU_ARE_NOT_SIGNED_IN))
                    .build();
        }
    }

    /**
     * Validates the email verification OTP.
     *
     * @param request The EmailConfirmationOtpSubmitRequest object containing the
     *                OTP ID and the OTP code.
     * @return The ApiResponse object with the result of the validation.
     */
    public AuthResponse valilidateEmailVerificationOtp(EmailConfirmationOtpSubmitRequest request) {
        val otpOptional = otpService.findById(request.getOtpId());
        if (otpOptional.isPresent()) {
            val otp = otpOptional.get();
            otpService.delete(otp);
            if (otp.getCode().equals(request.getCode()) && otp.getExpiryDate().after(new Date())) {
                val user = otp.getUser();
                if (!user.isEmailVerified()) {
                    val updatedUser = userRepository.save(user.withEmailVerified(true));
                    return AuthResponse.builder().errored(false).messages(List.of("Email verified."))
                            .build().withUserDetails(UserToResponseUserDetails.map(updatedUser));
                } else {
                    return AuthResponse.builder().errored(true).messages(List.of("Email already verified."))
                            .build();
                }
            }
        }
        return AuthResponse.builder().errored(true).messages(List.of(INVALID_OTP_OR_EXPIRED_PLEASE_TRY_AGAIN))
                .build();

    }

    /**
     * Requests an OTP (One-Time Password) for the given OTP request.
     *
     * @param request the OTP request containing the email or username
     * @return the response containing the OTP details and status
     */
    public OtpRequestResponse requestOTP(OtpRequest request) {
        val userOptional = userRepository.findByEmailOrUsername(request.getEmailOrUsername());
        if (userOptional.isPresent()) {
            val user = userOptional.get();
            val otp = otpService.createOTP(user);
            try {
                otpService.sendOTPEmail(user, otp, request.getMessage());
                return OtpToOtpResponse.map(user, otp);
            } catch (MailAuthenticationException | UnsupportedEncodingException
                    | MessagingException e) {
                return OtpRequestResponse.builder().errored(true)
                        .messages(List.of("Error sending verification email. Please try again later."))
                        .build();
            }
        } else {
            return OtpRequestResponse.builder().errored(true)
                    .messages(List.of("Provided email or username is invalid."))
                    .build();
        }
    }

    public AuthResponse updateUserDetails(UserDetailsUpdateRequest request) {
        var useIdOptional = getLogedinUserId();
        if (useIdOptional.isEmpty()) {
            return AuthResponse.builder().errored(true)
                    .messages(List.of(YOU_ARE_NOT_SIGNED_IN_PLEASE_SIGN_IN_AND_TRY_AGAIN))
                    .build();
        }
        val userId = useIdOptional.get();
        val otpOptional = otpService.findById(request.getOtpId());
        if (otpOptional.isPresent()) {
            val otp = otpOptional.get();
            otpService.delete(otp);
            val user = otp.getUser();
            if (user.getId().equals(userId) && otp.getCode().equals(request.getOtpCode())
                    && otp.getExpiryDate().after(new Date())) {
                updateUserFields(request, user);
                if (!user.isEmailVerified()) {
                    user.setEmailVerified(true);
                }
                val updatedUser = userRepository.save(user);
                val jwtAndRefreshToken = createJwtAndrefreshTokens(updatedUser);
                return UserToAuthResponse.map(updatedUser, jwtAndRefreshToken.getLeft(),
                        jwtAndRefreshToken.getRight());
            }
        }
        return AuthResponse.builder().errored(true)
                .messages(List.of(INVALID_OTP_OR_EXPIRED_PLEASE_TRY_AGAIN))
                .build();
    }

    /**
     * Reset the user's password.
     *
     * @param request the request object containing the OTP ID and the new password
     * @return the authentication response
     */
    public AuthResponse resetUserPassword(ResetUserPasswordRequest request) {
        val otpOptional = otpService.findById(request.getOtpId());
        if (otpOptional.isPresent()) {
            val otp = otpOptional.get();
            otpService.delete(otp);
            val user = otp.getUser();
            if ((user.getEmail().equals(request.getEmailOrUsername())
                    || user.getUsername().equals(request.getEmailOrUsername()))
                    && otp.getCode().equals(request.getOtpCode()) && otp.getExpiryDate().after(new Date())) {
                if (!user.isEmailVerified()) {
                    user.setEmailVerified(true);
                }
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                val updatedUser = userRepository.save(user);
                val jwtAndRefreshToken = createJwtAndrefreshTokens(updatedUser);
                return UserToAuthResponse.map(updatedUser, jwtAndRefreshToken.getLeft(),
                        jwtAndRefreshToken.getRight());
            }
        }
        return AuthResponse.builder().errored(true).messages(List.of(INVALID_OTP_OR_EXPIRED_PLEASE_TRY_AGAIN))
                .build();
    }

    /**
     * Updates the user fields based on the given UserDetailsUpdateRequest.
     *
     * @param request the UserDetailsUpdateRequest containing the updated user
     *                details
     * @param user    the User object to be updated
     */
    private void updateUserFields(UserDetailsUpdateRequest request, final User user) {
        if (request.getDisplayName() != null || !request.getDisplayName().isEmpty()) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getEmail() != null || !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatarUrl() != null || !request.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getPassword() != null || !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    /**
     * Retrieves the user id of the logged-in user.
     *
     * @return an Optional containing the user id of the logged-in user, or an
     *         empty Optional if the user is not logged in.
     */
    public Optional<String> getLogedinUserId() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        return Optional.ofNullable((String) authentication
                .getPrincipal());
    }

    /**
     * Creates a JWT token and refresh token for the given user.
     *
     * @param user the user object for which to create the tokens
     * @return a tuple containing the JWT token and the refresh token
     */
    public Pair<String, String> createJwtAndrefreshTokens(User user) {
        val jwt = jwtService.generateToken(user);
        val jwtRefreshToken = refreshTokenService.createRefreshToken(user);
        return Pair.of(jwt, jwtRefreshToken.getToken());
    }

    /**
     * Checks the registration request for validity and adds any errors to the
     * provided list.
     *
     * @param request the registration request to be checked
     * @param errors  the list of errors to add any validation errors to
     * @return the updated list of errors
     */
    private List<String> checkRegistrationRequest(SignupRequest request, List<String> errors) {
        val username = request.getUsername();
        val email = request.getEmail();
        val password = request.getPassword();

        if (isValidEmailFormat(email, errors)) {
            checkNewEmail(email, errors);
        }
        if (isValidUsernameFormat(username, errors)) {
            checkNewUsername(username, errors);
        }
        isValidPasswordFormat(password, errors);
        return errors;
    }

    /**
     * Checks the login request for any format errors and returns a list of errors.
     *
     * @param request the login request to be checked
     * @param errors  the list of errors to be populated
     * @return the list of errors after checking the login request
     */
    private List<String> checkLoginRequest(SigninRequest request, List<String> errors) {
        isValidEmailOrUsernameFormat(request.getEmailOrUsername(), errors);
        isValidPasswordFormat(request.getPassword(), errors);
        return errors;
    }

    /**
     * Checks if a new username is already taken and adds an error message to the
     * error list if it is.
     *
     * @param username the new username to be checked
     * @param errors   the list to which an error message is added if the username
     *                 is already taken
     * @return the list of errors after checking the username
     */
    private List<String> checkNewUsername(String username, List<String> errors) {
        if (userRepository.findByUsername(username).isPresent()) {
            errors.add(
                    "Username already taken. Please choose a different one or reset your password if you forgot it.");
        }
        return errors;
    }

    /**
     * Checks if the given email is already in use and adds an error message to the
     * list if it is.
     *
     * @param email  the email to check
     * @param errors the list to add the error message to
     * 
     * @return the list of errors after checking the email
     */
    private List<String> checkNewEmail(String email, List<String> errors) {
        if (userRepository.findByEmail(email).isPresent()) {
            errors.add("Email already in use. Please choose a different one or reset your password if you forgot it.");
        }
        return errors;
    }

    /**
     * Checks if the given username is in a valid format.
     *
     * @param username the username to be validated
     * @param errors   a list to store any validation errors
     * @return true if the username is valid, false otherwise
     */
    private boolean isValidUsernameFormat(String username, List<String> errors) {
        if (username == null || username.isBlank()) {
            errors.add("Username cannot be blank. Please type in a username.");
            return false;
        }
        username = username.trim();
        if (username.length() < 3) {
            errors.add("Username must be at least 3 characters long.");
            return false;
        }
        if (EMAIL_REGEX_PATTERN.matcher(username).matches()) {
            errors.add("Invalid username format. Might be confused with an email. Please type in a valid username.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given email is in a valid format.
     *
     * @param email  the email to be validated
     * @param errors a list to store any validation errors
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmailFormat(String email, List<String> errors) {
        if (email == null || email.isBlank()) {
            errors.add("Email cannot be blank. Please type in an email.");
            return false;
        }
        email = email.trim();
        if (!EMAIL_REGEX_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format. Please type in a valid email.");
            return false;
        }
        return true;
    }

    /**
     * Checks the validity of the email or username format.
     *
     * @param emailOrUsername the email or username to be checked
     * @param errors          a list to store any validation errors encountered
     * @return true if the email or username format is valid, false otherwise
     */
    private boolean isValidEmailOrUsernameFormat(String emailOrUsername, List<String> errors) {
        val emailErrors = new ArrayList<String>();
        val usernameErrors = new ArrayList<String>();
        if (isValidEmailFormat(emailOrUsername, emailErrors)
                || isValidUsernameFormat(emailOrUsername, usernameErrors)) {
            return true;
        }
        errors.addAll(emailErrors);
        errors.addAll(usernameErrors);
        return false;
    }

    /**
     * Checks if the given password is in a valid format.
     *
     * @param password the password to be checked
     * @param errors   a list to store any errors encountered
     * @return true if the password is in a valid format, false otherwise
     */
    private boolean isValidPasswordFormat(String password, List<String> errors) {
        if (password == null || password.isBlank()) {
            errors.add("Password cannot be blank. Please type in a password.");
            return false;
        }
        int digitCount = 0;
        int symbolCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            } else if (!Character.isLetter(c)) {
                symbolCount++;
            }
        }
        return checkPasswordErrors(password, errors, digitCount, symbolCount);

    }

    /**
     * Checks for password errors based on specific criteria.
     *
     * @param password    the password to be checked
     * @param errors      the list of errors to be updated
     * @param digitCount  the number of digits in the password
     * @param symbolCount the number of symbols in the password
     * @return true if there are password errors, false otherwise
     */
    private boolean checkPasswordErrors(String password, List<String> errors, int digitCount, int symbolCount) {
        boolean errored = false;
        if (password.length() < 8) {
            errors.add("Password must be at least 8 characters long.");
            errored = true;
        }
        if (digitCount < 2) {
            errors.add("Password must contain at least 2 digits.");
            errored = true;
        }
        if (symbolCount < 1) {
            errors.add("Password must contain at least 1 symbol.");
            errored = true;
        }
        return errored;
    }

    /**
     * Checks the refresh token request and adds any errors to the provided list.
     *
     * @param request the token refresh request
     * @param errors  the list of errors
     * @return the updated list of errors
     */
    public List<String> checkRefreshTokenRequest(TokenRefreshRequest request, List<String> errors) {
        if (request.getToken() == null || request.getToken().isBlank()) {
            errors.add("Token cannot be blank.");
        }
        return errors;
    }

}
