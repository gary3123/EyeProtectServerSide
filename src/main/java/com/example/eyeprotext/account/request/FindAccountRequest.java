package com.example.eyeprotext.account.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FindAccountRequest {
    private String accountId;
}
