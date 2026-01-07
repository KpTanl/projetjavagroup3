package com.group3.carrental.entity;
import jakarta.persistence.*;

@Entity
public class Assurance {
    private @Id @GeneratedValue Long id;
    private String nom;
    private String grilleTarifaire;

    public Assurance(String nom, String grilleTarifaire) {
        this.nom = nom;
        this.grilleTarifaire = grilleTarifaire;
    }

    public void importerGrille() { }

    public double calculerPrixTotal(Vehicule v) {
        return 0.0;
    }
}
