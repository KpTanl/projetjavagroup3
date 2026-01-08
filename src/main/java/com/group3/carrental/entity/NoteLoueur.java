package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "note_loueur")
public class NoteLoueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double noteTraitementVehicule;
    private double noteEngagement;
    private double noteResponsabilite;
    private String commentaire;

    @Column(name = "note_globale")
    private double noteGlobale;

    @ManyToOne
    @JoinColumn(name = "loueur_id")
    private Loueur loueur;

    public NoteLoueur() {}

    public double calculerMoyenne() {
        return (noteTraitementVehicule + noteEngagement + noteResponsabilite) / 3.0;
    }
}
