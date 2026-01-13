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
    private Date dateFin;
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
        this.dateFin = dateFin;
        this.agent = agent;
        this.loueur = loueur;
        this.vehicule = vehicule;
        this.prixTotal = prixTotal;
    }

    public void genererPdf() {
    }

    public class StatutContrat {
    }

    public boolean estTermine() {
        if (this.dateFin == null)
            return false;
        return this.dateFin.before(new Date());
    }

    public boolean estAccepte() {
        return this.statut == Statut.Accepte;
    }

    public double calculerPrixAjuste() {
        double prixCible = this.prixTotal;

        if (vehicule.getOptionRetour() == Vehicule.OptionRetour.retour_parking &&
                vehicule.getParkingPartenaire() != null) {
            double reduction = vehicule.getParkingPartenaire().getReductionloueur();
            prixCible = prixCible * (1 - reduction);
        }

        return Math.round(prixCible * 100.0) / 100.0;
    }

}
