package com.example.bookexchange.data.dto;

import lombok.Data;

/**
 * DTO class for authentication (login) request.
 */

@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
    private String signedData;
    private String email;
}
