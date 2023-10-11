# Documentation for API for FxcpServerApplication

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *http://localhost*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *AuthenticateApi* | [**authenticate**](Apis/AuthenticateApi.md#authenticate) | **POST** /api/v1/auth/public/authenticate |  |
| *GetAUsersSocialLinksApi* | [**getUserSocialLinks**](Apis/GetAUsersSocialLinksApi.md#getusersociallinks) | **GET** /api/v1/social-links/user |  |
| *GetMultipleUsersSocialLinksApi* | [**getUsersSocialLinks**](Apis/GetMultipleUsersSocialLinksApi.md#getuserssociallinks) | **GET** /api/v1/social-links/users |  |
| *GetSocialLinksApi* | [**getCurrentUserSocialLinks**](Apis/GetSocialLinksApi.md#getcurrentusersociallinks) | **GET** /api/v1/social-links |  |
| *RefreshTokenApi* | [**refeshToken**](Apis/RefreshTokenApi.md#refeshtoken) | **POST** /api/v1/auth/public/refresh-token |  |
| *RegisterApi* | [**register**](Apis/RegisterApi.md#register) | **POST** /api/v1/auth/public/register |  |
| *ResetUserPasswordApi* | [**resetUserPassword**](Apis/ResetUserPasswordApi.md#resetuserpassword) | **POST** /api/v1/auth/public/reset-password |  |
| *SendCustomEmailVerificationApi* | [**requestOtp**](Apis/SendCustomEmailVerificationApi.md#requestotp) | **POST** /api/v1/auth/public/request-otp |  |
| *SendEmailVerificationMailApi* | [**sendVerificationEmail**](Apis/SendEmailVerificationMailApi.md#sendverificationemail) | **POST** /api/v1/auth/authenticated/send-email-verification-mail |  |
| *SetSocialLinksApi* | [**updateCurrentUserSocialLinks**](Apis/SetSocialLinksApi.md#updatecurrentusersociallinks) | **POST** /api/v1/social-links |  |
| *SignoutApi* | [**signout**](Apis/SignoutApi.md#signout) | **POST** /api/v1/auth/authenticated/logout |  |
| *UpdateUserDetailsApi* | [**updateUserDetails**](Apis/UpdateUserDetailsApi.md#updateuserdetails) | **POST** /api/v1/auth/authenticated/update-user-details |  |
| *ValidateEmailVerificationWithOtpApi* | [**valilidateEmailVerificationOtp**](Apis/ValidateEmailVerificationWithOtpApi.md#valilidateemailverificationotp) | **POST** /api/v1/auth/public/validate-email-verification-otp |  |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AuthResponse](./Models/AuthResponse.md)
 - [Date](./Models/Date.md)
 - [EmailConfirmationOtpSubmitRequest](./Models/EmailConfirmationOtpSubmitRequest.md)
 - [MultiUserSocialLinksRequest](./Models/MultiUserSocialLinksRequest.md)
 - [MultiUserSocialLinksResponse](./Models/MultiUserSocialLinksResponse.md)
 - [OtpRequest](./Models/OtpRequest.md)
 - [OtpRequestResponse](./Models/OtpRequestResponse.md)
 - [PayloadSocialLinks](./Models/PayloadSocialLinks.md)
 - [Permission](./Models/Permission.md)
 - [ResetUserPasswordRequest](./Models/ResetUserPasswordRequest.md)
 - [ResponseUserDetails](./Models/ResponseUserDetails.md)
 - [Role](./Models/Role.md)
 - [SigninRequest](./Models/SigninRequest.md)
 - [SignoutRequest](./Models/SignoutRequest.md)
 - [SignupRequest](./Models/SignupRequest.md)
 - [SingleUserSocialLinksRequest](./Models/SingleUserSocialLinksRequest.md)
 - [SingleUserSocialLinksResponse](./Models/SingleUserSocialLinksResponse.md)
 - [SocialLinkSharingScope](./Models/SocialLinkSharingScope.md)
 - [TokenRefreshRequest](./Models/TokenRefreshRequest.md)
 - [UserDetailsUpdateRequest](./Models/UserDetailsUpdateRequest.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="Authentication"></a>
### Authentication

- **Type**: HTTP Bearer Token authentication (JWT)

