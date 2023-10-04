package com.bran.service.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bran.service.auth.model.payload.ApiResponse;
import com.bran.service.auth.model.payload.request.EmailConfirmationOtpSubmitRequest;
import com.bran.service.auth.model.payload.request.OtpRequest;
import com.bran.service.auth.model.payload.request.SigninRequest;
import com.bran.service.auth.model.payload.request.SignoutRequest;
import com.bran.service.auth.model.payload.request.SignupRequest;
import com.bran.service.auth.model.payload.request.TokenRefreshRequest;
import com.bran.service.auth.model.payload.request.UserDetailsUpdateRequest;
import com.bran.service.auth.model.payload.response.OtpRequestResponse;
import com.bran.service.auth.model.payload.response.SigninResponse;
import com.bran.service.auth.model.payload.response.SignupResponse;
import com.bran.service.auth.model.payload.response.TokenRefreshResponse;
import com.bran.service.auth.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Tag(name = "Register", description = "Register a new user")
    @PostMapping(value = "/public/register")
    public ResponseEntity<SignupResponse> register(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Tag(name = "Authenticate", description = "Authenticate a user")
    @PostMapping(value = "/public/authenticate")
    public ResponseEntity<SigninResponse> authenticate(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Tag(name = "Refresh-token", description = "Refresh a user's jwt token")
    @PostMapping(value = "/public/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refeshToken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Tag(name = "Signout", description = "Sign out a user")
    @PostMapping(value = "/authenticated/logout")
    public ResponseEntity<ApiResponse> signout(@RequestBody SignoutRequest request) {
        return ResponseEntity.ok(authService.signout(request));
    }

    @Tag(name = "Send email verification mail", description = "Send an email verification mail with OTP")
    @PostMapping(value = "/authenticated/send-email-verification-mail")
    public ResponseEntity<ApiResponse> sendVerificationEmail() {
        return ResponseEntity.ok(authService.sendEmailVerificationEmail());
    }

    @Tag(name = "Validate email verification", description = "Validate an email verification OTP")
    @PostMapping(value = "/public/validate-email-verification")
    public ResponseEntity<ApiResponse> validateEmailVerification(
            @RequestBody EmailConfirmationOtpSubmitRequest request) {
        return ResponseEntity.ok(authService.valilidateEmailVerificationOtp(request));
    }

    @Tag(name = "Send custom email verification", description = "Send an email verification mail with OTP with custom message")
    @PostMapping(value = "/public/request-otp")
    public ResponseEntity<OtpRequestResponse> postMethodName(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(authService.requestOTP(request));
    }

    @Tag(name = "Update user details", description = "Update user details, requires OTP verification")
    @PostMapping(value = "/authenticated/update-user-details")
    public ResponseEntity<ApiResponse> updateUserDetails(@RequestBody UserDetailsUpdateRequest request) {
        return ResponseEntity.ok(authService.updateUserDetails(request));
    }

}
