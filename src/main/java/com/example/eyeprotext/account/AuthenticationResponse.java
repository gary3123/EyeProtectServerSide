package com.example.eyeprotext.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse<D> {
    private int result;
    private D data;
    private String message;
    private String token;
}
