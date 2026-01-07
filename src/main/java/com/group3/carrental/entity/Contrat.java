package com.group3.carrental.entity;

import java.util.Date;

public class Contrat {
    private String id;
    private Date dateDeb;
    private Date DateFin;
    private Agent agent;
    private Loueur loueur;
    private double prixTotal;


    public Contrat(String id, Date dateDeb, Date dateFin, Agent agent, Loueur loueur, double prixTotal) {
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
