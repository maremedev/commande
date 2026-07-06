package com.polytech.commandes.service;

import com.polytech.commandes.dto.ProduitDTO;
import com.polytech.commandes.dto.ProduitRequestDTO;
import com.polytech.commandes.dto.ProduitVenteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProduitService {

    ProduitDTO creer(ProduitRequestDTO requestDTO);

    ProduitDTO obtenirParId(Long id);

    Page<ProduitDTO> listerTous(Pageable pageable);

    ProduitDTO mettreAJour(Long id, ProduitRequestDTO requestDTO);

    void supprimer(Long id);

    Page<ProduitDTO> rechercher(BigDecimal minPrix, BigDecimal maxPrix, Integer stockMin, Pageable pageable);

    List<ProduitVenteDTO> top5ProduitsLesPlusVendus();
}
