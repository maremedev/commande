package com.polytech.commandes.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;
    private String username;
    private String tokenType; // "Bearer" par convention
}