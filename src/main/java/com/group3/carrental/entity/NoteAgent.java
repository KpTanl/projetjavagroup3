package com.group3.carrental.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "note_agent")
public class NoteAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String critere;
    private String commentaire;

    @Column(name = "note_globale")
    private int noteGlobale;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public NoteAgent() {}
}
