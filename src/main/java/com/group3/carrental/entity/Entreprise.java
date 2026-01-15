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
public class Entreprise extends Compte implements Services {

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

    public double definirTarif(String type, String modele, double prix) {

        return 0.0;
    }

    public void importerFichierTarifs() {
        
    }

    public void nettoyer(Vehicule v) {
        
    }

    @Override
    public void souscrire() {
        
    }

    @Override
    public void resilier() {
        
    }

    @Override
    public void renouveler() {
        
    }
}
