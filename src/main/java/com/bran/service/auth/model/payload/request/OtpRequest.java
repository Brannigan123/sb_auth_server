package com.bran.service.auth.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    private String emailOrUsername;
    private String message;
}
