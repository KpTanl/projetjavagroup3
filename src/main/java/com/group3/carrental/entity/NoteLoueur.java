package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "note_loueur")
public class NoteLoueur extends Note {

    private int noteTraitementVehicule;
    private int noteEngagement;
    private int noteResponsabilite;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "loueur_id", nullable = false)
    private Loueur loueur;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent auteur;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    public NoteLoueur(
            int noteTraitementVehicule,
            int noteEngagement,
            int noteResponsabilite,
            String commentaire,
            Loueur loueur,
            Agent auteur,
            Contrat contrat
    ) {
        super(commentaire);
        this.noteTraitementVehicule = noteTraitementVehicule;
        this.noteEngagement = noteEngagement;
        this.noteResponsabilite = noteResponsabilite;
        this.loueur = loueur;
        this.auteur = auteur;
        this.contrat = contrat;
        this.noteGlobale = calculerNoteGlobale();
    }

    @Override
    public double calculerNoteGlobale() {
        return (noteTraitementVehicule + noteEngagement + noteResponsabilite) / 3.0;
    }
}