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

    public abstract boolean seConnecter(String motDePasseSaisi);
}
