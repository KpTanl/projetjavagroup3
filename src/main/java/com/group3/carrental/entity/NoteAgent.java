package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "note_agent")
public class NoteAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int noteGestionVehicule;
    private int noteBienveillance;
    private int noteReactivite;
    private String commentaire;

    @Column(name = "note_globale")
    private double noteGlobale = (noteGestionVehicule + noteBienveillance + noteReactivite) / 3.0;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public NoteAgent() {
    }

    public NoteAgent(int noteGestionVehicule, int noteBienveillance, int noteReactivite, String commentaire,
            double noteGlobale, Agent agent) {
        this.noteGestionVehicule = noteGestionVehicule;
        this.noteBienveillance = noteBienveillance;
        this.noteReactivite = noteReactivite;
        this.commentaire = commentaire;
        this.noteGlobale = noteGlobale;
        this.agent = agent;
    }

}
