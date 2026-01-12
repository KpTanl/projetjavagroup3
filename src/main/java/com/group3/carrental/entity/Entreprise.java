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

    private String siret; // UML: siret, Code previously: Nsiret
    private String nom; // UML: nom, Code previously: RaisonSoc

    @Column(columnDefinition = "TEXT")
    private String catalogueTarifs;

    public Entreprise(int id, String email, String motDePasse, String nom, String siret, String catalogueTarifs) {
        super(id, email, motDePasse);
        this.nom = nom;
        this.siret = siret;
        this.catalogueTarifs = catalogueTarifs;
    }

    public double definirTarif(String type, String modele, double prix) {
        // TODO : Implémenter la logique de tarification
        return 0.0;
    }

    public void importerFichierTarifs() {
        // TODO : Implémenter la logique d'importation de fichiers
    }

    public void nettoyer(Vehicule v) {
        // TODO: Implémenter le nettoyage
    }

    @Override
    public void Souscrire() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Resilier() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Renouveler() {
        // TODO Auto-generated method stub
    }
}
