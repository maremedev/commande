package com.polytech.commandes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiredDate;

    @Builder.Default
    private Boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
}