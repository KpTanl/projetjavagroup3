package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicule_notes")
public class NoteVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int noteProprete;
    private int noteUsure;
    private int noteConfort;
    private String commentaire;

    @Column(name = "note_globale")
    private double noteGlobale = (noteProprete + noteUsure + noteConfort) / 3.0;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;


    public NoteVehicule() {}

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort, String commentaire) {
    this.noteProprete = noteProprete;
    this.noteUsure = noteUsure;
    this.noteConfort = noteConfort;
    this.commentaire = commentaire;
    }

    public double calculerNoteGlobale() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }
}
