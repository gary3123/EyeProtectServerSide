package com.example.eyeprotext.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class GetAccountPersonInformationResponse {
    private UUID accountId;
    private String email;
    private String name;
    private String dor;
    private String image;
}
