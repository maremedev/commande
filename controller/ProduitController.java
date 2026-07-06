package com.polytech.commandes.controller;

import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.dto.ProduitRequestDTO;
import com.polytech.commandes.dto.ProduitVenteDTO;
import com.polytech.commandes.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Gestion des produits (CRUD, recherche, statistiques)")
public class ProduitController {

    private final ProduitService produitService;

    @Operation(summary = "Créer un produit")
    @PostMapping
    public ResponseEntity<ProduitDTO> creer(@Valid @RequestBody ProduitRequestDTO requestDTO) {
        ProduitDTO cree = produitService.creer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cree);
    }

    @Operation(summary = "Obtenir un produit par son id")
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.obtenirParId(id));
    }

    @Operation(summary = "Lister les produits avec pagination et tri")
    // Exemple : GET /api/produits?page=0&size=10&sort=nom,asc
    @GetMapping
    public ResponseEntity<Page<ProduitDTO>> listerTous(Pageable pageable) {
        return ResponseEntity.ok(produitService.listerTous(pageable));
    }

    @Operation(summary = "Mettre à jour un produit")
    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> mettreAJour(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequestDTO requestDTO) {
        return ResponseEntity.ok(produitService.mettreAJour(id, requestDTO));
    }

    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        produitService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recherche multicritère de produits")
    // Exemple : GET /api/produits/search?minPrix=100&maxPrix=500&stockMin=1&page=0&size=10&sort=prix,asc
    @GetMapping("/search")
    public ResponseEntity<Page<ProduitDTO>> rechercher(
            @RequestParam(required = false) BigDecimal minPrix,
            @RequestParam(required = false) BigDecimal maxPrix,
            @RequestParam(required = false) Integer stockMin,
            Pageable pageable) {
        return ResponseEntity.ok(produitService.rechercher(minPrix, maxPrix, stockMin, pageable));
    }

    @Operation(summary = "BONUS — Top 5 des produits les plus vendus")
    @GetMapping("/statistiques/top5-ventes")
    public ResponseEntity<List<ProduitVenteDTO>> top5ProduitsLesPlusVendus() {
        return ResponseEntity.ok(produitService.top5ProduitsLesPlusVendus());
    }
}