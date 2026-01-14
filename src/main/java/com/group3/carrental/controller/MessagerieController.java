package com.group3.carrental.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Message;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.ServiceMessagerie;

@Component
public class MessagerieController {

    private static final Scanner sc = new Scanner(System.in);
    private final ServiceMessagerie serviceMessagerie;

    @Autowired
    public MessagerieController(ServiceMessagerie serviceMessagerie) {
        this.serviceMessagerie = serviceMessagerie;
    }

    public void displayMenuMessagerie(Utilisateur currentUser) {
        if (currentUser == null) {
            System.out.println("Accès refusé : vous devez être connecté.");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Messagerie ---");
            System.out.println("1. Envoyer un message (par nom)");
            System.out.println("2. Voir ma boîte de réception");
            System.out.println("3. Voir une conversation (par nom)");
            System.out.println("0. Retour");

            int choice = lireInt();
            switch (choice) {
                case 1 -> menuEnvoyerMessage(currentUser);
                case 2 -> menuAfficherInbox(currentUser);
                case 3 -> menuAfficherConversation(currentUser);
                case 0 -> back = true;
                default -> System.out.println("Choix invalide !");
            }
        }
    }

    private void menuEnvoyerMessage(Utilisateur currentUser) {
        Utilisateur destinataire = choisirUtilisateurParNom(currentUser);
        if (destinataire == null) return;

        System.out.print("Contenu du message : ");
        String contenu = sc.nextLine();

        try {
            Message msg = serviceMessagerie.envoyerMessage(
                    currentUser.getId(),
                    destinataire.getId(),
                    contenu
            );
            System.out.println("Message envoyé ! (id=" + msg.getId() + ", date=" + msg.getDateEnvoi() + ")");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void menuAfficherInbox(Utilisateur currentUser) {
        try {
            List<Message> inbox = serviceMessagerie.consulterMessages(currentUser.getId());
            if (inbox.isEmpty()) {
                System.out.println("(Aucun message reçu)");
                return;
            }

            System.out.println("\n--- Boîte de réception ---");
            for (Message m : inbox) {
                System.out.println("[" + m.getDateEnvoi() + "] de "
                        + m.getExpediteur().getPrenom() + " " + m.getExpediteur().getNom()
                        + " : " + m.getContenu());
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void menuAfficherConversation(Utilisateur currentUser) {
        Utilisateur other = choisirUtilisateurParNom(currentUser);
        if (other == null) return;

        try {
            List<Message> conv = serviceMessagerie.consulterConversation(currentUser.getId(), other.getId());
            if (conv.isEmpty()) {
                System.out.println("(Aucun message entre vous)");
                return;
            }

            System.out.println("\n--- Conversation avec " + other.getPrenom() + " " + other.getNom() + " (id=" + other.getId() + ") ---");
            for (Message m : conv) {
                String who = (m.getExpediteur().getId() == currentUser.getId()) ? "Moi" : m.getExpediteur().getPrenom();
                System.out.println("[" + m.getDateEnvoi() + "] " + who + " : " + m.getContenu());
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private Utilisateur choisirUtilisateurParNom(Utilisateur currentUser) {
        System.out.print("Nom de la personne : ");
        String nom = sc.nextLine().trim();

        System.out.print("Prénom (optionnel - Entrée pour ignorer) : ");
        String prenom = sc.nextLine().trim();

        List<Utilisateur> candidats;
        try {
            if (!prenom.isEmpty()) {
                candidats = serviceMessagerie.rechercherUtilisateursParNomPrenom(nom, prenom);
            } else {
                candidats = serviceMessagerie.rechercherUtilisateursParNom(nom);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }

        // enlever soi-même
        candidats = candidats.stream()
                .filter(u -> u.getId() != currentUser.getId())
                .toList();

        if (candidats.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé avec ce nom/prénom.");
            return null;
        }

        System.out.println("\n--- Résultats ---");
        for (int i = 0; i < candidats.size(); i++) {
            Utilisateur u = candidats.get(i);
            System.out.println((i + 1) + ". " + u.getPrenom() + " " + u.getNom()
                    + " | id=" + u.getId()
                    + " | role=" + u.getRole()
                    + " | email=" + u.getEmail());
        }

        System.out.print("Choisir une personne (numéro 1-" + candidats.size() + ") : ");
        int choix = lireInt();

        if (choix < 1 || choix > candidats.size()) {
            System.out.println("Choix invalide.");
            return null;
        }

        return candidats.get(choix - 1);
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