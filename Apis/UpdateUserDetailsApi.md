# UpdateUserDetailsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**resetUserPassword**](UpdateUserDetailsApi.md#resetUserPassword) | **POST** /api/v1/auth/public/reset-password |  |
| [**updateUserDetails**](UpdateUserDetailsApi.md#updateUserDetails) | **POST** /api/v1/auth/authenticated/update-user-details |  |


<a name="resetUserPassword"></a>
# **resetUserPassword**
> AuthResponse resetUserPassword(ResetUserPasswordRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ResetUserPasswordRequest** | [**ResetUserPasswordRequest**](../Models/ResetUserPasswordRequest.md)|  | |

### Return type

[**AuthResponse**](../Models/AuthResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="updateUserDetails"></a>
# **updateUserDetails**
> AuthResponse updateUserDetails(UserDetailsUpdateRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **UserDetailsUpdateRequest** | [**UserDetailsUpdateRequest**](../Models/UserDetailsUpdateRequest.md)|  | |

### Return type

[**AuthResponse**](../Models/AuthResponse.md)

### Authorization

[Authentication](../README.md#Authentication)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

