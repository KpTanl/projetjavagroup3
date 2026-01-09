package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicule_notes")
public class NoteVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_proprete", nullable = false)
    private int noteProprete;

    @Column(name = "note_usure", nullable = false)
    private int noteUsure;

    @Column(name = "note_confort", nullable = false)
    private int noteConfort;

    private String commentaire;

    @Column(name = "note_globale", nullable = false)
    private double noteGlobale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort, String commentaire) {
        this.noteProprete = noteProprete;
        this.noteUsure = noteUsure;
        this.noteConfort = noteConfort;
        this.commentaire = commentaire;
        this.noteGlobale = calculerNoteGlobale();
    }

    public double calculerNoteGlobale() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }

    @PrePersist
    @PreUpdate
    private void updateNoteGlobale() {
        this.noteGlobale = calculerNoteGlobale();
    }
}