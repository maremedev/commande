package com.polytech.commandes.service;

import com.polytech.commandes.dto.*;
import com.polytech.commandes.entity.*;
import com.polytech.commandes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;

    @Override
    public CommandeResponseDTO creerCommande(
            CommandeRequestDTO dto) {

        Client client = clientRepository
            .findById(dto.getClientId())
            .orElseThrow(() -> 
                new RuntimeException("Client non trouvé"));

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatus(Status.CREATED);

        return toDTO(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponseDTO ajouterLigne(
            Long commandeId, 
            LigneCommandeRequestDTO dto) {

        Commande commande = commandeRepository
            .findById(commandeId)
            .orElseThrow(() -> 
                new RuntimeException("Commande non trouvée"));

        // Bloquer si commande déjà validée ou annulée
        if (commande.getStatus() != Status.CREATED) {
            throw new RuntimeException(
                "Impossible de modifier une commande " 
                + commande.getStatus()
            );
        }

        Produit produit = produitRepository
            .findById(dto.getProduitId())
            .orElseThrow(() -> 
                new RuntimeException("Produit non trouvé"));

        LigneCommande ligne = new LigneCommande();
        ligne.setCommande(commande);
        ligne.setProduit(produit);
        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaire(produit.getPrix());

        ligneRepository.save(ligne);

        return toDTO(commande);
    }

    @Override
    public CommandeResponseDTO validerCommande(Long id) {

        Commande commande = commandeRepository
            .findById(id)
            .orElseThrow(() -> 
                new RuntimeException("Commande non trouvée"));

        if (commande.getStatus() != Status.CREATED) {
            throw new RuntimeException(
                "Commande déjà " + commande.getStatus()
            );
        }

        // Vérifier et décrémenter le stock
        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();

            if (produit.getStock() < ligne.getQuantite()) {
                throw new RuntimeException(
                    "Stock insuffisant pour : " 
                    + produit.getNom()
                );
            }
            produit.setStock(
                produit.getStock() - ligne.getQuantite()
            );
            produitRepository.save(produit);
        }

        commande.setStatus(Status.VALIDATED);
        return toDTO(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponseDTO annulerCommande(Long id) {

        Commande commande = commandeRepository
            .findById(id)
            .orElseThrow(() -> 
                new RuntimeException("Commande non trouvée"));

        if (commande.getStatus() != Status.CREATED) {
            throw new RuntimeException(
                "Impossible d'annuler une commande " 
                + commande.getStatus()
            );
        }

        commande.setStatus(Status.CANCELLED);
        return toDTO(commandeRepository.save(commande));
    }

    @Override
    public List<CommandeResponseDTO> getCommandesByClient(
            Long clientId) {
        return commandeRepository
            .findByClientId(clientId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommandeResponseDTO> getCommandesEntreDates(
            LocalDateTime debut, LocalDateTime fin) {
        return commandeRepository
            .findByDateCommandeBetween(debut, fin)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getChiffreAffaires() {
        BigDecimal ca = commandeRepository
            .calculerChiffreAffaires();
        return ca != null ? ca : BigDecimal.ZERO;
    }

    @Override
    public CommandeResponseDTO getCommandeById(Long id) {
        return toDTO(commandeRepository
            .findById(id)
            .orElseThrow(() -> 
                new RuntimeException("Commande non trouvée"))
        );
    }

    @Override
    public List<CommandeResponseDTO> getAllCommandes() {
        return commandeRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    // Convertir Commande → DTO
    private CommandeResponseDTO toDTO(Commande commande) {
        CommandeResponseDTO dto = new CommandeResponseDTO();
        dto.setId(commande.getId());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatus(commande.getStatus());
        dto.setClientId(commande.getClient().getId());
        dto.setClientNom(commande.getClient().getNom());

        // Calculer le total
        if (commande.getLignes() != null) {
            BigDecimal total = commande.getLignes()
                .stream()
                .map(l -> l.getPrixUnitaire()
                    .multiply(BigDecimal.valueOf(
                        l.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotal(total);
        }
        return dto;
    }
}