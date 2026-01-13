package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;

@Component
public class UtilisateurController {
    private static final Scanner sc = new Scanner(System.in);
    private final UtilisateurService utilisateurService;
    private final ContratService contratService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, ContratService contratService) {
        this.utilisateurService = utilisateurService;
        this.contratService = contratService;
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
}
