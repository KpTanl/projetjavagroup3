package com.group3.carrental.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDeb;
    private Date DateFin;
    @ManyToOne
    private Agent agent;
    @ManyToOne
    private Loueur loueur;
    @ManyToOne
    private Vehicule vehicule;
    private double prixTotal;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    public enum Statut {
        Presigne,
        Accepte,
        Refuse
    }

    public Contrat(Date dateDeb, Date dateFin, Agent agent, Loueur loueur, Vehicule vehicule, double prixTotal) {
        this.dateDeb = dateDeb;
        this.DateFin = dateFin;
        this.agent = agent;
        this.loueur = loueur;
        this.vehicule = vehicule;
        this.prixTotal = prixTotal;
    }

    public void genererPdf() {

    }

    public class StatutContrat {
    }
    // Dans Contrat.java

public double calculerPrixAjuste() {
    double prixCible = this.prixTotal;

    // 1. On vérifie si le véhicule est associé à un parking
    if (vehicule.getOptionRetour() == Vehicule.OptionRetour.retour_parking && 
        vehicule.getParkingPartenaire() != null) {
        
        // 2. On récupère le taux de réduction du parking
        double reduction = vehicule.getParkingPartenaire().getReductionloueur();
        
        // 3. On applique la réduction
        prixCible = prixCible * (1 - reduction);
        
        System.out.println("Option Parking détectée ! Réduction de " + (reduction * 100) + "% appliquée.");
    }
    
    return Math.round(prixCible * 100.0) / 100.0; // Arrondi à 2 décimales
}

}
