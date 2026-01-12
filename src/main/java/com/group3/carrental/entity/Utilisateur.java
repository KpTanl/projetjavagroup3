package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
// Requis par Hibernate/JPA
@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    @Column(unique = true, nullable = false)
    private String email;
    private String motDePasse;
    @Enumerated(EnumType.STRING)
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

    public void seConnecter() {

    }

    public void modifierProfil() {

    }

    public void signerContrat() {

    }

}