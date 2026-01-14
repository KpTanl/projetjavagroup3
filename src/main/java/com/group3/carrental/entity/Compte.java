package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false, unique = true)
    protected String email;

    protected String motDePasse;

    public Compte(int id, String email, String motDePasse) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    /**
     * Vérifie si le mot de passe fourni correspond au mot de passe du compte
     * 
     * @param motDePasseSaisi Le mot de passe à vérifier
     * @return true si le mot de passe est correct, false sinon
     */
    public boolean seConnecter(String motDePasseSaisi) {
        return this.motDePasse != null && this.motDePasse.equals(motDePasseSaisi);
    }
}
