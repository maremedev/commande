package com.polytech.commandes.controller;

import com.polytech.commandes.dto.*;
import com.polytech.commandes.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    // Créer une commande
    @PostMapping
    public ResponseEntity<CommandeResponseDTO> creer(
            @RequestBody CommandeRequestDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commandeService.creerCommande(dto));
    }

    // Voir une commande
    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            commandeService.getCommandeById(id)
        );
    }

    // Voir toutes les commandes
    @GetMapping
    public ResponseEntity<List<CommandeResponseDTO>> getAll() {
        return ResponseEntity.ok(
            commandeService.getAllCommandes()
        );
    }

    // Ajouter une ligne à une commande
    @PostMapping("/{id}/lignes")
    public ResponseEntity<CommandeResponseDTO> ajouterLigne(
            @PathVariable Long id,
            @RequestBody LigneCommandeRequestDTO dto) {
        return ResponseEntity.ok(
            commandeService.ajouterLigne(id, dto)
        );
    }

    // Valider une commande
    @PutMapping("/{id}/valider")
    public ResponseEntity<CommandeResponseDTO> valider(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            commandeService.validerCommande(id)
        );
    }

    // Annuler une commande
    @PutMapping("/{id}/annuler")
    public ResponseEntity<CommandeResponseDTO> annuler(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            commandeService.annulerCommande(id)
        );
    }

    // Commandes d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeResponseDTO>> 
            parClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(
            commandeService.getCommandesByClient(clientId)
        );
    }

    // Commandes entre deux dates
    @GetMapping("/entre-dates")
    public ResponseEntity<List<CommandeResponseDTO>> 
            entreDates(
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime debut,
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE_TIME) 
                LocalDateTime fin) {
        return ResponseEntity.ok(
            commandeService.getCommandesEntreDates(
                debut, fin)
        );
    }

    // Chiffre d'affaires global
    @GetMapping("/statistiques/chiffre-affaires")
    public ResponseEntity<BigDecimal> chiffreAffaires() {
        return ResponseEntity.ok(
            commandeService.getChiffreAffaires()
        );
    }
}