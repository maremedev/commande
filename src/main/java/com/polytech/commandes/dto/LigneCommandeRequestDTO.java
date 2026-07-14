package com.polytech.commandes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LigneCommandeRequestDTO {
    private Long produitId;
    private Integer quantite;
}