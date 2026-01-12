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

}
