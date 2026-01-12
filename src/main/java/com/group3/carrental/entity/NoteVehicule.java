package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
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

    public void setVehicule(Vehicule vehicule) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVehicule'");
    }
}
