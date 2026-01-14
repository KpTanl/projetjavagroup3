package com.group3.carrental.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "discussion_note_messages")
public class DiscussionNoteMessage {

    public enum Cible {
        NOTE_VEHICULE,
        NOTE_AGENT,
        NOTE_LOUEUR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cible cible;

    @Column(nullable = false, length = 2000)
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Utilisateur auteur;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;

    public DiscussionNoteMessage(Contrat contrat, Utilisateur auteur, Cible cible, String contenu) {
        this.contrat = contrat;
        this.auteur = auteur;
        this.cible = cible;
        this.contenu = contenu;
        this.dateCreation = LocalDateTime.now();
    }
}