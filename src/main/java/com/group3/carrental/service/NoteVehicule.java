package com.group3.carrental.service;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class NoteVehicule {
    // Critères de notation
    private int noteProprete;
    private int noteUsure;
    private int noteConfort;
    private String commentaire;

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort, String commentaire) {
        this.noteProprete = noteProprete;
        this.noteUsure = noteUsure;
        this.noteConfort = noteConfort;
        this.commentaire = commentaire;
    }

    /**
     * Calcule la moyenne des critères pour obtenir la note globale
     */
    public double calculerNoteGlobale() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }
}
