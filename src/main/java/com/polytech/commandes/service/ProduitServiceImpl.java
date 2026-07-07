package com.polytech.commandes.service;

import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.dto.ProduitRequestDTO;
import com.polytech.commandes.dto.ProduitVenteDTO;
import com.polytech.commandes.entity.Produit;
import com.polytech.commandes.exception.DuplicateResourceException;
import com.polytech.commandes.exception.ResourceNotFoundException;
import com.polytech.commandes.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    @Override
    public ProduitDTO creer(ProduitRequestDTO requestDTO) {
        if (produitRepository.existsByNom(requestDTO.getNom())) {
            throw new DuplicateResourceException(
                    "Un produit avec le nom '" + requestDTO.getNom() + "' existe déjà");
        }

        Integer stock = requestDTO.getStock();
        Produit produit = Produit.builder()
                .nom(requestDTO.getNom())
                .prix(requestDTO.getPrix())
                .stock(stock != null ? stock : 0)
                .build();

        Produit sauvegarde = produitRepository.save(produit);
        return toDTO(sauvegarde);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitDTO obtenirParId(Long id) {
        return toDTO(getProduitOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProduitDTO> listerTous(Pageable pageable) {
        return produitRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public ProduitDTO mettreAJour(Long id, ProduitRequestDTO requestDTO) {
        Produit produit = getProduitOrThrow(id);

        if (!produit.getNom().equals(requestDTO.getNom())
                && produitRepository.existsByNom(requestDTO.getNom())) {
            throw new DuplicateResourceException(
                    "Un produit avec le nom '" + requestDTO.getNom() + "' existe déjà");
        }

        produit.setNom(requestDTO.getNom());
        produit.setPrix(requestDTO.getPrix());
        if (requestDTO.getStock() != null) {
            produit.setStock(requestDTO.getStock());
        }

        return toDTO(produitRepository.save(produit));
    }

    @Override
    public void supprimer(Long id) {
        Produit produit = getProduitOrThrow(id);
        produitRepository.delete(produit);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProduitDTO> rechercher(BigDecimal minPrix, BigDecimal maxPrix, Integer stockMin, Pageable pageable) {
        return produitRepository.search(minPrix, maxPrix, stockMin, pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitVenteDTO> top5ProduitsLesPlusVendus() {
        Pageable top5 = PageRequest.of(0, 5);
        return produitRepository.findProduitsLesPlusVendus(top5)
                .map(p -> new ProduitVenteDTO(p.getProduitId(), p.getNomProduit(), p.getQuantiteTotaleVendue()))
                .getContent();
    }

    private Produit getProduitOrThrow(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'id : " + id));
    }

    private ProduitDTO toDTO(Produit produit) {
        return ProduitDTO.builder()
                .id(produit.getId())
                .nom(produit.getNom())
                .prix(produit.getPrix())
                .stock(produit.getStock())
                .build();
    }
}
