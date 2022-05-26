package com.equp.back.backend.dto;

import lombok.Data;

/**
 * DTO class for authentication (login) request.
 *
 * @author Roman Ungefuk
 * @version 1.0
 */

@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}