package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public class Utilisateur extends Compte {

    private String nom;
    private String prenom;

    @Enumerated(EnumType.STRING)
    protected Role role;
    protected double latitudeHabitation;
    protected double longitudeHabitation;

    // Parrainage (Referral System)
    @ManyToOne
    @JoinColumn(name = "parrain_id")
    protected Utilisateur parrain;

    protected double soldePorteMonnaie = 0.0;

    protected boolean bonusParrainageRecu = false;

    public enum Role {
        Loueur,
        Agent
    }

    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse, Role role) {
        super(id, email, motDePasse);
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse, Role role,
            double latitudeHabitation, double longitudeHabitation) {
        super(id, email, motDePasse);
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.latitudeHabitation = latitudeHabitation;
        this.longitudeHabitation = longitudeHabitation;
    }

    public void modifierProfil(String nom, String prenom, String email, String motDePasse) {
        if (nom != null && !nom.isEmpty()) {
            this.nom = nom;
        }
        if (prenom != null && !prenom.isEmpty()) {
            this.prenom = prenom;
        }
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
        if (motDePasse != null && !motDePasse.isEmpty()) {
            this.motDePasse = motDePasse;
        }
    }

}