package com.polytech.commandes.service;

import com.polytech.commandes.config.JwtService;
import com.polytech.commandes.dto.AuthResponseDTO;
import com.polytech.commandes.dto.LoginRequestDTO;
import com.polytech.commandes.dto.RegisterRequestDTO;
import com.polytech.commandes.entity.Client;
import com.polytech.commandes.entity.Role;
import com.polytech.commandes.entity.Utilisateur;
import com.polytech.commandes.repository.ClientRepository;
import com.polytech.commandes.repository.RoleRepository;
import com.polytech.commandes.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (utilisateurRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Ce username est déjà utilisé");
        }
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                ));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        Utilisateur utilisateur = Utilisateur.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();
        utilisateur = utilisateurRepository.save(utilisateur);

        Client client = Client.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .utilisateur(utilisateur)
                .build();
        clientRepository.save(client);

        String token = jwtService.generateToken(toUserDetails(utilisateur));

        return AuthResponseDTO.builder()
                .token(token)
                .username(utilisateur.getUsername())
                .tokenType("Bearer")
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        String token = jwtService.generateToken(toUserDetails(utilisateur));

        return AuthResponseDTO.builder()
                .token(token)
                .username(utilisateur.getUsername())
                .tokenType("Bearer")
                .build();
    }

    private UserDetails toUserDetails(Utilisateur utilisateur) {
        return org.springframework.security.core.userdetails.User
                .withUsername(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .authorities(utilisateur.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new))
                .build();
    }
}