package com.polytech.commandes.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entite, Long id) {
        super(entite + " introuvable avec id : " + id);
    }
}