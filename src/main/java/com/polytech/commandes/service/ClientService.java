package com.polytech.commandes.service;

import com.polytech.commandes.dto.ClientRequestDTO;
import com.polytech.commandes.dto.ClientResponseDTO;
import com.polytech.commandes.entity.Client;
import com.polytech.commandes.exception.ResourceNotFoundException;
import com.polytech.commandes.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        return toResponseDTO(client);
    }

    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));

        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());

        Client updated = clientRepository.save(client);
        return toResponseDTO(updated);
    }

    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private ClientResponseDTO toResponseDTO(Client client) {
        return ClientResponseDTO.builder()
                .id(client.getId())
                .nom(client.getNom())
                .email(client.getEmail())
                .username(client.getUtilisateur() != null ? client.getUtilisateur().getUsername() : null)
                .build();
    }
}