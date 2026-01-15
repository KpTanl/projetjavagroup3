package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.HashMap;

@Data
@NoArgsConstructor
@Entity
public class PrestataireEntretien implements Services {

    @Id
    private String siret;

    private String nomSociete;

    private String activite;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "prestataire_tarifs", joinColumns = @JoinColumn(name = "prestataire_siret"))
    @MapKeyColumn(name = "modele_vehicule")
    @Column(name = "prix")
    private Map<String, Float> cataloguePrixE = new HashMap<>();

    public PrestataireEntretien(String siret, String nomSociete, String activite) {
        this.siret = siret;
        this.nomSociete = nomSociete;
        this.activite = activite;
        this.cataloguePrixE = new HashMap<>();
    }

    // --- MÉTHODES DU DIAGRAMME ---

    public void definirTarif(String modele, float prix) {
        this.cataloguePrixE.put(modele, prix);
    }

    public void nettoyer(Vehicule v) {
        System.out.println("Nettoyage du véhicule " + v.getModele() + " par " + this.nomSociete);
    }

    // --- MÉTHODES DE L'INTERFACE SERVICES ---

    @Override
    public void souscrire() {
        System.out.println("Le prestataire " + nomSociete + " rejoint le réseau.");
    }

    @Override
    public void resilier() {
        System.out.println("Le prestataire " + nomSociete + " quitte le réseau.");
    }

    @Override
    public void renouveler() {
        System.out.println("Renouvellement du contrat prestataire.");
    }
}
