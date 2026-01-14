package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "note_agent")
public class NoteAgent extends Note {

    private int noteGestionVehicule;
    private int noteBienveillance;
    private int noteReactivite;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "loueur_id", nullable = false)
    private Loueur auteur;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    public NoteAgent(
            int noteGestionVehicule,
            int noteBienveillance,
            int noteReactivite,
            String commentaire,
            Agent agent,
            Loueur auteur,
            Contrat contrat
    ) {
        super(commentaire);
        this.noteGestionVehicule = noteGestionVehicule;
        this.noteBienveillance = noteBienveillance;
        this.noteReactivite = noteReactivite;
        this.agent = agent;
        this.auteur = auteur;
        this.contrat = contrat;
        this.noteGlobale = calculerNoteGlobale();
    }

    @Override
    public double calculerNoteGlobale() {
        return (noteGestionVehicule + noteBienveillance + noteReactivite) / 3.0;
    }
}