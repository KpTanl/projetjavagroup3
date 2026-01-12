package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String commentaire;

    @Column(name = "note_globale")
    protected double noteGlobale;

    public Note(String commentaire) {
        this.commentaire = commentaire;
    }

    public abstract double calculerNoteGlobale();
}
