package com.group3.carrental.entity;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Entreprise")
public class Entreprise {
    private @Id @GeneratedValue Long id;
    private String nom;
    private String catalogueTarifs;


    public Entreprise(String nom, String catalogueTarifs) {
        this.nom = nom;
        this.catalogueTarifs = catalogueTarifs;
    }

    public double definirTarif(String type, String modele) {
        return (Double) null;
    }

    public void importerFichierTarifs() {

    }

    public void seConnecter() {

    }
}
