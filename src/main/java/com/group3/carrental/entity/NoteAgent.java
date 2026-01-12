package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "note_agent")
public class NoteAgent extends Note {

    private double noteGestionVehicule;
    private double noteBienveillance;
    private double noteReactivite;
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public NoteAgent() {}

    public NoteAgent(double noteGestionVehicule, double noteBienveillance, double noteReactivite, String commentaire,Agent agent) {
        this.noteGestionVehicule = noteGestionVehicule;
        this.noteBienveillance = noteBienveillance;
        this.noteReactivite = noteReactivite;
        this.commentaire = commentaire;
        this.agent = agent;
    }

    @Override
    public double calculerNoteMoyenne() {
        return (noteGestionVehicule + noteBienveillance + noteReactivite) / 3.0;
    }
}
