package com.bran.service.auth.model.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

@With
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    protected boolean errored;
    protected List<String> messages;
}
