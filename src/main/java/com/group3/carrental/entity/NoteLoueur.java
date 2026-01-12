package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "note_loueur")
public class NoteLoueur extends Note {

    private double noteTraitementVehicule;
    private double noteEngagement;
    private double noteResponsabilite;
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "loueur_id")
    private Loueur loueur;

    public NoteLoueur() {}

    public NoteLoueur(double noteTraitementVehicule, double noteEngagement, double noteResponsabilite, String commentaire, Loueur loueur) {
        this.noteTraitementVehicule = noteTraitementVehicule;
        this.noteEngagement = noteEngagement;
        this.noteResponsabilite = noteResponsabilite;
        this.commentaire = commentaire;
        this.loueur = loueur;
    }

    @Override
    public double calculerNoteMoyenne() {
        return (noteTraitementVehicule + noteEngagement + noteResponsabilite) / 3.0;
    }
}
