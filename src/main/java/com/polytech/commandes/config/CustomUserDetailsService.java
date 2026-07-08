package com.polytech.commandes.config;

import com.polytech.commandes.entity.Utilisateur;
import com.polytech.commandes.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        List<GrantedAuthority> authorities = utilisateur.getRoles().stream()
                .<GrantedAuthority>map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new User(
                utilisateur.getUsername(),
                utilisateur.getPassword(),
                utilisateur.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}