package com.polytech.commandes.dto;

import com.polytech.commandes.entity.Status;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommandeResponseDTO {

    private Long id;
    private LocalDateTime dateCommande;
    private Status status;
    private Long clientId;
    private String clientNom;
    private BigDecimal total;
}