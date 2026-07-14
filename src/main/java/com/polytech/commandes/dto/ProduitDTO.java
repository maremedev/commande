package com.polytech.commandes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO exposé par l'API pour représenter un produit.
 * Ne jamais exposer directement l'entité Produit dans les réponses REST.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {

    private Long id;
    private String nom;
    private BigDecimal prix;
    private Integer stock;
}

