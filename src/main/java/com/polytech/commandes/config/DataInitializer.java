package com.polytech.commandes.config;

import com.polytech.commandes.entity.*;
import com.polytech.commandes.repository.ClientRepository;
import com.polytech.commandes.repository.CommandeRepository;
import com.polytech.commandes.repository.ProduitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Initialisation des donnees de demonstration.
 * Actif UNIQUEMENT sur le profil "dev".
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(
            ProduitRepository produitRepository,
            ClientRepository clientRepository,
            CommandeRepository commandeRepository) {

        return args -> {

            if (produitRepository.count() > 0) {
                log.info("Donnees deja presentes, initialisation ignoree.");
                return;
            }

            log.info("Initialisation des donnees de demonstration (profil dev)...");

            // --- Produits ---
            Produit laptop = Produit.builder()
                    .nom("Ordinateur portable")
                    .description("Laptop 15 pouces, 16 Go RAM")
                    .prix(new BigDecimal("650000"))
                    .stock(15)
                    .build();

            Produit souris = Produit.builder()
                    .nom("Souris sans fil")
                    .description("Souris ergonomique sans fil")
                    .prix(new BigDecimal("8500"))
                    .stock(50)
                    .build();

            Produit clavier = Produit.builder()
                    .nom("Clavier mecanique")
                    .description("Clavier mecanique retroeclaire")
                    .prix(new BigDecimal("25000"))
                    .stock(30)
                    .build();

            produitRepository.saveAll(List.of(laptop, souris, clavier));

            // --- Clients ---
            Client client1 = Client.builder()
                    .nom("Awa Ndiaye")
                    .email("awa.ndiaye@example.com")
                    .build();

            Client client2 = Client.builder()
                    .nom("Moussa Fall")
                    .email("moussa.fall@example.com")
                    .build();

            clientRepository.saveAll(List.of(client1, client2));

            // --- Commande complete avec ses lignes ---
            Commande commande = new Commande();
            commande.setDateCommande(LocalDateTime.now());
            commande.setStatus(Status.CREATED);
            commande.setClient(client1);

            LigneCommande ligne1 = new LigneCommande();
            ligne1.setProduit(laptop);
            ligne1.setQuantite(1);
            ligne1.setPrixUnitaire(laptop.getPrix());
            ligne1.setCommande(commande);

            LigneCommande ligne2 = new LigneCommande();
            ligne2.setProduit(souris);
            ligne2.setQuantite(2);
            ligne2.setPrixUnitaire(souris.getPrix());
            ligne2.setCommande(commande);

            commande.setLignes(List.of(ligne1, ligne2));

            // Le cascade ALL sur "lignes" persiste automatiquement les LigneCommande
            commandeRepository.save(commande);

            log.info("Initialisation terminee : {} produits, {} clients, 1 commande complete avec {} lignes.",
                    produitRepository.count(), clientRepository.count(), commande.getLignes().size());
        };
    }
}