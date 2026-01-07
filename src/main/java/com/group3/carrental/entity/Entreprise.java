package com.group3.carrental.entity;

public class Entreprise {
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
