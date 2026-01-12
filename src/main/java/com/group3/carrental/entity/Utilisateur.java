package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// Requis par Hibernate/JPA
@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public abstract class Utilisateur extends Compte {

    private String nom;
    private String prenom;

    @Enumerated(EnumType.STRING)
    protected Role role;

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

    public void seConnecter() {

    }

    public void modifierProfil() {
        // TODO: Implement logic
    }

    public void signerContrat() {

    }

}