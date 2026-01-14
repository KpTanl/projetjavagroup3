package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "note_vehicule", uniqueConstraints = @UniqueConstraint(columnNames = {"contrat_id"}))
public class NoteVehicule extends Note {

    private int noteProprete;
    private int noteUsure;
    private int noteConfort;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "loueur_id", nullable = false)
    private Loueur auteur;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort,
                        String commentaire,
                        Vehicule vehicule,
                        Loueur auteur,
                        Contrat contrat) {

        super(commentaire);
        this.noteProprete = noteProprete;
        this.noteUsure = noteUsure;
        this.noteConfort = noteConfort;
        this.vehicule = vehicule;
        this.auteur = auteur;
        this.contrat = contrat;
        this.noteGlobale = calculerNoteGlobale();
    }

    @Override
    public double calculerNoteGlobale() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }
    
}