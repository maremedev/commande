package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Optional<Produit> findByNom(String nom);

    boolean existsByNom(String nom);

    /**
     * Recherche multicritère avec pagination/tri gérés par le Pageable.
     * GET /api/produits/search?minPrix=&maxPrix=&stockMin=
     */
    @Query("""
            SELECT p FROM Produit p
            WHERE (:minPrix IS NULL OR p.prix >= :minPrix)
              AND (:maxPrix IS NULL OR p.prix <= :maxPrix)
              AND (:stockMin IS NULL OR p.stock >= :stockMin)
            """)
    Page<Produit> search(
            @Param("minPrix") BigDecimal minPrix,
            @Param("maxPrix") BigDecimal maxPrix,
            @Param("stockMin") Integer stockMin,
            Pageable pageable
    );

    /**
     * BONUS (III.1) — Top produits les plus vendus.
     * NB : dépend de l'entité LigneCommande (module "Commandes" — Personne 3).
     * Utiliser un Pageable de taille 5 (PageRequest.of(0, 5)) côté service pour le "Top 5".
     */
    @Query("""
            SELECT lc.produit.id AS produitId,
                   lc.produit.nom AS nomProduit,
                   SUM(lc.quantite) AS quantiteTotaleVendue
            FROM LigneCommande lc
            GROUP BY lc.produit.id, lc.produit.nom
            ORDER BY SUM(lc.quantite) DESC
            """)
    Page<ProduitVenteProjection> findProduitsLesPlusVendus(Pageable pageable);

    interface ProduitVenteProjection {
        Long getProduitId();
        String getNomProduit();
        Long getQuantiteTotaleVendue();
    }
}
