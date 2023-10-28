package com.example.eyeprotext.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FindAccountResponse {
    private UUID accountId;
    private String name;
    private String image;
}
