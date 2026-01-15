package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;


@Data
@NoArgsConstructor
@Entity
@Table(name = "assurance")
public class Assurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    
    @Column(length = 1000)
    private String grilleTarifaire; 
    
    private double prixParJour;

    public Assurance(String nom, String grilleTarifaire, double prixParJour) {
        this.nom = nom;
        this.grilleTarifaire = grilleTarifaire;
        this.prixParJour = prixParJour;
    }

    
    public void importerGrille(String grilleFormatee) {
        this.grilleTarifaire = grilleFormatee;
    }

    /*Calculer le prix pour un véhicule selon son type et modèle*/
    
    public double calculerPrixTotal(Vehicule vehicule, int nbJours) {
        double prixJournalier = getPrixPourVehicule(vehicule);
        return prixJournalier * nbJours;
    }
    

    public double getPrixPourVehicule(Vehicule vehicule) {
        if (grilleTarifaire == null || grilleTarifaire.isEmpty()) {
            return prixParJour;
        }
        
        Map<String, Double> grille = parseGrille();
        
        String cleSpecifique = vehicule.getType() + "-" + vehicule.getModele();
        if (grille.containsKey(cleSpecifique)) {
            return grille.get(cleSpecifique);
        }
        
        String cleType = vehicule.getType().toString();
        if (grille.containsKey(cleType)) {
            return grille.get(cleType);
        }
        
        return prixParJour;
    }
    

    public Map<String, Double> parseGrille() {
        Map<String, Double> grille = new HashMap<>();
        
        if (grilleTarifaire == null || grilleTarifaire.isEmpty()) {
            return grille;
        }
        
        String[] entries = grilleTarifaire.split(",");
        for (String entry : entries) {
            String[] parts = entry.trim().split(":");
            if (parts.length == 2) {
                try {
                    grille.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                } catch (NumberFormatException e) {
                    // Ignorer les entrées mal formatées
                }
            }
        }
        
        return grille;
    }
}
