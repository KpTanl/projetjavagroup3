package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.NoteService;
import com.group3.carrental.service.UtilisateurService;

@Component
public class UtilisateurController {
    private static final Scanner sc = new Scanner(System.in);
    private final UtilisateurService utilisateurService;
    private final ContratService contratService;
    private final NoteService noteService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, ContratService contratService, NoteService noteService) {
        this.utilisateurService = utilisateurService;
        this.contratService = contratService;
        this.noteService = noteService;
    }

    public void afficherMonProfil(Utilisateur currentUser) {
        if (currentUser == null) {
            System.out.println("Accès refusé : vous devez être connecté.");
            return;
        }

        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Mon profil ---");
            System.out.println("ID          : " + currentUser.getId());
            System.out.println("Nom         : " + currentUser.getNom());
            System.out.println("Prénom      : " + currentUser.getPrenom());
            System.out.println("Email       : " + currentUser.getEmail());
            System.out.println("Mot de passe: " + currentUser.getMotDePasse());

            System.out.println("\n1. Modifier nom");
            System.out.println("2. Modifier prénom");
            System.out.println("3. Modifier email");
            System.out.println("4. Modifier mot de passe");
            System.out.println("5. Historique des locations");
            System.out.println("6. Noter");
            System.out.println("0. Retour");
            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nouveau nom : ");
                    currentUser.setNom(sc.nextLine());
                    utilisateurService.mettreAJour(currentUser);
                    System.out.println("Nom mis à jour.");
                    break;
                case 2:
                    System.out.print("Nouveau prénom : ");
                    currentUser.setPrenom(sc.nextLine());
                    utilisateurService.mettreAJour(currentUser);
                    System.out.println("Prénom mis à jour.");
                    break;
                case 3:
                    System.out.print("Nouvel email : ");
                    currentUser.setEmail(sc.nextLine());
                    utilisateurService.mettreAJour(currentUser);
                    System.out.println("Email mis à jour.");
                    break;
                case 4:
                    System.out.print("Nouveau mot de passe : ");
                    currentUser.setMotDePasse(sc.nextLine());
                    utilisateurService.mettreAJour(currentUser);
                    System.out.println("Mot de passe mis à jour.");
                    break;
                case 5:
                    afficherHistoriqueLocations(currentUser);
                    break;
                case 6:
                    menuNotation(currentUser);
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void afficherHistoriqueLocations(Utilisateur currentUser) {
        if (currentUser == null) {
            System.out.println("Accès refusé : vous devez être connecté.");
            return;
        }

        List<Contrat> contrats = contratService.getContratsParLoueur(currentUser.getId());
        if (contrats.isEmpty()) {
            System.out.println("Aucune location trouvée.");
            return;
        }

        System.out.println("\n--- Historique des locations ---");
        for (Contrat c : contrats) {
            LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Contrat #" + c.getId() + " : du " + deb + " au " + fin);

            if (c.getVehicule() != null) {
                System.out.println("  Véhicule : " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
                System.out.println("  Lieu     : " + c.getVehicule().getLocalisationComplete());
            }

            if (c.getAgent() != null) {
                System.out.println("  Agent    : " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
            }
            System.out.println("  Prix total : " + c.getPrixTotal() + "€");
            System.out.println("------------------------------------");
        }
    }
    public void menuNotation(Utilisateur currentUser) {
        if (currentUser instanceof Loueur loueur) {
            noterDepuisLoueur(loueur);
        } else if (currentUser instanceof Agent agent) {
            noterDepuisAgent(agent);
        } else {
            System.out.println("Rôle non supporté.");
        }
    }

    // ==========================
    // Loueur -> note Vehicule/Agent
    // ==========================
    private void noterDepuisLoueur(Loueur loueur) {
        List<Contrat> termines = contratService.getContratsTerminesAccepteesParLoueur(loueur.getId());
        if (termines.isEmpty()) {
            System.out.println("Aucun contrat terminé et accepté à noter.");
            return;
        }

        System.out.println("\n--- Contrats terminés (notables) ---");
        for (Contrat c : termines) {
            System.out.println("Contrat #" + c.getId()
                    + " | Véhicule=" + c.getVehicule().getMarque() + " " + c.getVehicule().getModele()
                    + " | Agent=" + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
        }

        System.out.print("Entrer l'ID du contrat à noter : ");
        Long contratId = sc.nextLong();
        sc.nextLine();

        System.out.println("1. Noter le véhicule");
        System.out.println("2. Noter l'agent");
        int choix = sc.nextInt();
        sc.nextLine();

        try {
            if (choix == 1) {
                System.out.print("Propreté (1-5) : ");
                int p = sc.nextInt();
                System.out.print("Usure (1-5) : ");
                int u = sc.nextInt();
                System.out.print("Confort (1-5) : ");
                int c = sc.nextInt();
                sc.nextLine();
                System.out.print("Commentaire : ");
                String com = sc.nextLine();

                noteService.noterVehicule(contratId, loueur, p, u, c, com);
                System.out.println("Note véhicule enregistrée.");
            } else if (choix == 2) {
                System.out.print("Gestion véhicule (1-5) : ");
                int g = sc.nextInt();
                System.out.print("Bienveillance (1-5) : ");
                int b = sc.nextInt();
                System.out.print("Réactivité (1-5) : ");
                int r = sc.nextInt();
                sc.nextLine();
                System.out.print("Commentaire : ");
                String com = sc.nextLine();

                noteService.noterAgent(contratId, loueur, g, b, r, com);
                System.out.println("Note agent enregistrée.");
            } else {
                System.out.println("Choix invalide.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ==========================
    // Agent -> note Loueur
    // ==========================
    private void noterDepuisAgent(Agent agent) {
        List<Contrat> termines = contratService.getContratsTerminesAccepteesParAgent(agent.getId());
        if (termines.isEmpty()) {
            System.out.println("Aucun contrat terminé et accepté à noter.");
            return;
        }

        System.out.println("\n--- Contrats terminés (notables) ---");
        for (Contrat c : termines) {
            System.out.println("Contrat #" + c.getId()
                    + " | Loueur=" + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom()
                    + " | Véhicule=" + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
        }

        System.out.print("Entrer l'ID du contrat à noter : ");
        Long contratId = sc.nextLong();
        sc.nextLine();

        try {
            System.out.print("Traitement véhicule (1-5) : ");
            int t = sc.nextInt();
            System.out.print("Engagement (1-5) : ");
            int e = sc.nextInt();
            System.out.print("Responsabilité (1-5) : ");
            int r = sc.nextInt();
            sc.nextLine();
            System.out.print("Commentaire : ");
            String com = sc.nextLine();

            noteService.noterLoueur(contratId, agent, t, e, r, com);
            System.out.println("Note loueur enregistrée.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
