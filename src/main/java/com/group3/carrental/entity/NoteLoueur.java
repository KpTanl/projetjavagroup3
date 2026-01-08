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

    private int noteTraitementVehicule;
    private int noteEngagement;
    private int noteResponsabilite;
    private String commentaire;

    @Column(name = "note_globale")
    private double noteGlobale =  (noteTraitementVehicule + noteEngagement + noteResponsabilite) / 3.0;

    @ManyToOne
    @JoinColumn(name = "loueur_id")
    private Loueur loueur;

    public NoteLoueur() {}

}
