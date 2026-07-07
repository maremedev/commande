package com.polytech.commandes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilisé pour le classement des produits les plus vendus (bonus III.1).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProduitVenteDTO {

    private Long produitId;
    private String nomProduit;
    private Long quantiteTotaleVendue;
}
