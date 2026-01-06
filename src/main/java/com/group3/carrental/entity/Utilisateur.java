package com.group3.carrental.entity;

import lombok.Data;

@Data
// (Lombok) Generate getters setters et toString automatically
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    protected Role role;

    public enum Role {
        Loueur,
        Agent
    }

    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

}