package com.polytech.commandes.service;

import com.polytech.commandes.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CommandeService {

    CommandeResponseDTO creerCommande(
        CommandeRequestDTO dto
    );

    CommandeResponseDTO getCommandeById(Long id);

    List<CommandeResponseDTO> getAllCommandes();

    CommandeResponseDTO ajouterLigne(
        Long commandeId, 
        LigneCommandeRequestDTO dto
    );

    CommandeResponseDTO validerCommande(Long id);

    CommandeResponseDTO annulerCommande(Long id);

    List<CommandeResponseDTO> getCommandesByClient(
        Long clientId
    );

    List<CommandeResponseDTO> getCommandesEntreDates(
        LocalDateTime debut, 
        LocalDateTime fin
    );

    BigDecimal getChiffreAffaires();
}