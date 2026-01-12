package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicule_notes")
public class NoteVehicule extends Note {

    @Column(nullable = false)
    private int noteProprete;

    @Column(nullable = false)
    private int noteUsure;

    @Column(nullable = false)
    private int noteConfort;

    @Column(length = 2000)
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort, String commentaire,Vehicule vehicule) {
        this.noteProprete = noteProprete;
        this.noteUsure = noteUsure;
        this.noteConfort = noteConfort;
        this.commentaire = commentaire;
        this.vehicule = vehicule;
    }

    @Override
    public double calculerNoteMoyenne() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }
}
