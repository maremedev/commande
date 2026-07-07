package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface CommandeRepository 
    extends JpaRepository<Commande, Long> {

    // Commandes d'un client
    List<Commande> findByClientId(Long clientId);

    // Commandes entre deux dates
    List<Commande> findByDateCommandeBetween(
        LocalDateTime debut, 
        LocalDateTime fin
    );

    // Chiffre d'affaires global
    @Query("SELECT SUM(l.quantite * l.prixUnitaire) " +
           "FROM LigneCommande l " +
           "WHERE l.commande.status = 'VALIDATED'")
    BigDecimal calculerChiffreAffaires();
}