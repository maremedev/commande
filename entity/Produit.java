package com.polytech.commandes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    // Relation N-1 vers Utilisateur (créateur du produit).
    // NB : l'entité Utilisateur appartient au module "Clients & Sécurité" (Personne 2).
    // Ce champ suppose qu'elle existe déjà dans le squelette commun (com.polytech.commandes.entity.Utilisateur).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createur_id")
    private Utilisateur createur;
}

