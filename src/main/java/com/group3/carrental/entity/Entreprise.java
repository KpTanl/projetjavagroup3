package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Entreprise")
public class Entreprise {
    @Id
    private String Nsiret;
    private String RaisonSoc;
    @Column(columnDefinition = "TEXT")
    private String catalogueTarifs;

    public Entreprise(String Nsiret, String RaisonSoc, String catalogueTarifs) {
        this.Nsiret = Nsiret;
        this.RaisonSoc = RaisonSoc;
        this.catalogueTarifs = catalogueTarifs;
    }

    public double definirTarif(String type, String modele) {
        return 0.0;
    }

    public void importerFichierTarifs() {
    }

    public void seConnecter() {
    }
}
