package com.polytech.commandes.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {

    private Long id;
    private String nom;
    private String email;
    private String username; // vient de l'Utilisateur associé, sans exposer le password
}