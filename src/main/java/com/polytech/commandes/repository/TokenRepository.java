package com.polytech.commandes.repository;

import com.polytech.commandes.entity.Token;
import com.polytech.commandes.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query("""
        SELECT t FROM Token t
        WHERE t.utilisateur = :utilisateur
        AND t.revoked = false
    """)
    List<Token> findAllValidTokensByUtilisateur(Utilisateur utilisateur);
}