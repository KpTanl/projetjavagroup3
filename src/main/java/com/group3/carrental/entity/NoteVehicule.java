package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "vehicule_notes")
public class NoteVehicule extends Note {

    @Column(name = "note_proprete", nullable = false)
    private int noteProprete;

    @Column(name = "note_usure", nullable = false)
    private int noteUsure;

    @Column(name = "note_confort", nullable = false)
    private int noteConfort;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    public NoteVehicule(int noteProprete, int noteUsure, int noteConfort, String commentaire) {
        super(commentaire);
        this.noteProprete = noteProprete;
        this.noteUsure = noteUsure;
        this.noteConfort = noteConfort;
        this.noteGlobale = calculerNoteGlobale();
    }

    @Override
    public double calculerNoteGlobale() {
        return (noteProprete + noteUsure + noteConfort) / 3.0;
    }
}
