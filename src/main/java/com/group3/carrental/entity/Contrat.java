package com.group3.carrental.entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Contrat")
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
    private double prixTotal;

    public Contrat() {
    }

    public Contrat(Long id, Date dateDeb, Date dateFin, Agent agent, Loueur loueur, double prixTotal) {
        this.id = id;
        this.dateDeb = dateDeb;
        DateFin = dateFin;
        this.agent = agent;
        this.loueur = loueur;
        this.prixTotal = prixTotal;
    }

    public void genererPdf() {

    }

}
