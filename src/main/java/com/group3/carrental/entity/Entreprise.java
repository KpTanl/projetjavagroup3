package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Entreprise")
public class Entreprise extends Compte {

    private String siret;
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String catalogueTarifs;

    public Entreprise(int id, String email, String motDePasse, String nom, String siret, String catalogueTarifs) {
        super(id, email, motDePasse);
        this.nom = nom;
        this.siret = siret;
        this.catalogueTarifs = catalogueTarifs;
    }

    @Override
    public boolean seConnecter(String motDePasseSaisi) {
        return this.motDePasse != null && this.motDePasse.equals(motDePasseSaisi);
    }
}
