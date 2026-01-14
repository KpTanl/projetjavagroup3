package com.group3.carrental.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.group3.carrental.entity.DiscussionNoteMessage;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.DiscussionNoteMessage.Cible;
import com.group3.carrental.service.DiscussionNoteService;

@Component
public class DiscussionNoteController {

    private static final Scanner sc = new Scanner(System.in);

    private final DiscussionNoteService discussionNoteService;

    public DiscussionNoteController(DiscussionNoteService discussionNoteService) {
        this.discussionNoteService = discussionNoteService;
    }

    public void afficherDiscussion(Long contratId) {
        List<DiscussionNoteMessage> msgs = discussionNoteService.getDiscussion(contratId);
        if (msgs.isEmpty()) {
            System.out.println("(Aucun message de discussion pour ce contrat.)");
            return;
        }

        System.out.println("\n--- Discussion (toutes notes) ---");
        for (DiscussionNoteMessage m : msgs) {
            System.out.println("[" + m.getDateCreation() + "] (" + m.getCible() + ") "
                    + m.getAuteur().getPrenom() + " : " + m.getContenu());
        }
    }

    public void menuDiscussion(Utilisateur currentUser, Long contratId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Discussion sur notes | Contrat #" + contratId + " ===");
            System.out.println("1. Voir discussion complète");
            System.out.println("2. Voir discussion Note Véhicule");
            System.out.println("3. Voir discussion Note Agent");
            System.out.println("4. Voir discussion Note Loueur");
            System.out.println("5. Répondre / Ajouter un message");
            System.out.println("0. Retour");

            int choix = lireInt();
            try {
                switch (choix) {
                    case 1 -> afficherDiscussion(contratId);
                    case 2 -> afficherParCible(contratId, Cible.NOTE_VEHICULE);
                    case 3 -> afficherParCible(contratId, Cible.NOTE_AGENT);
                    case 4 -> afficherParCible(contratId, Cible.NOTE_LOUEUR);
                    case 5 -> ajouterMessage(currentUser, contratId);
                    case 0 -> back = true;
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void afficherParCible(Long contratId, Cible cible) {
        List<DiscussionNoteMessage> msgs = discussionNoteService.getDiscussionParCible(contratId, cible);
        if (msgs.isEmpty()) {
            System.out.println("(Aucun message pour " + cible + ")");
            return;
        }
        System.out.println("\n--- Discussion " + cible + " ---");
        for (DiscussionNoteMessage m : msgs) {
            System.out.println("[" + m.getDateCreation() + "] "
                    + m.getAuteur().getPrenom() + " : " + m.getContenu());
        }
    }

    private void ajouterMessage(Utilisateur currentUser, Long contratId) {
        System.out.println("\nChoisir la note ciblée :");
        System.out.println("1. Note Véhicule");
        System.out.println("2. Note Agent");
        System.out.println("3. Note Loueur");
        int c = lireInt();

        Cible cible = switch (c) {
            case 1 -> Cible.NOTE_VEHICULE;
            case 2 -> Cible.NOTE_AGENT;
            case 3 -> Cible.NOTE_LOUEUR;
            default -> throw new IllegalArgumentException("Choix cible invalide.");
        };

        System.out.print("Votre réponse : ");
        String contenu = sc.nextLine();

        discussionNoteService.ajouterMessage(contratId, currentUser, cible, contenu);
        System.out.println("Message ajouté.");
    }

    private int lireInt() {
        while (true) {
            try {
                String s = sc.nextLine();
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.print("Veuillez entrer un nombre : ");
            }
        }
    }
}