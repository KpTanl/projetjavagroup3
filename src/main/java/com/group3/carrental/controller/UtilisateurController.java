package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Parking;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.NoteAffichageService;
import com.group3.carrental.service.NoteService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class UtilisateurController {

    private static final Scanner sc = new Scanner(System.in);

    private final UtilisateurService utilisateurService;
    private final ContratService contratService;
    private final NoteService noteService;
    private final NoteAffichageService noteAffichageService;
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService,
            ContratService contratService,
            NoteService noteService,
            NoteAffichageService noteAffichageService,
            VehiculeService vehiculeService,
            AssuranceService assuranceService) {
        this.utilisateurService = utilisateurService;
        this.contratService = contratService;
        this.noteService = noteService;
        this.noteAffichageService = noteAffichageService;
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
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
            System.out.println("7. Afficher notes d'un contrat");
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

                case 7:
                    System.out.print("ID du contrat : ");
                    Long contratId = sc.nextLong();
                    sc.nextLine();
                    afficherNotesContrat(contratId);
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

        if (currentUser instanceof Loueur loueur) {
            List<Contrat> contrats = contratService.getContratsParLoueur(loueur.getId());
            if (contrats.isEmpty()) {
                System.out.println("Aucune location trouvée.");
                return;
            }

            System.out.println("\n--- Historique des locations (Loueur) ---");
            for (Contrat c : contrats) {
                LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                System.out.println(
                        "Contrat #" + c.getId() + " : du " + deb + " au " + fin + " | statut=" + c.getStatut());

                if (c.getVehicule() != null) {
                    System.out
                            .println("  Véhicule : " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
                    System.out.println("  Lieu     : " + c.getVehicule().getLocalisationComplete());
                }
                if (c.getAgent() != null) {
                    System.out.println("  Agent    : " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
                }
                System.out.println("  Prix total : " + c.getPrixTotal() + "€");
                System.out.println("------------------------------------");
            }
            return;
        }

        if (currentUser instanceof Agent agent) {
            List<Contrat> contrats = contratService.getContratsParAgent(agent.getId());
            if (contrats.isEmpty()) {
                System.out.println("Aucun contrat trouvé.");
                return;
            }

            System.out.println("\n--- Historique des contrats (Agent) ---");
            for (Contrat c : contrats) {
                LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                System.out.println(
                        "Contrat #" + c.getId() + " : du " + deb + " au " + fin + " | statut=" + c.getStatut());

                if (c.getLoueur() != null) {
                    System.out.println("  Loueur : " + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom());
                }
                if (c.getVehicule() != null) {
                    System.out
                            .println("  Véhicule : " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
                }
                System.out.println("------------------------------------");
            }
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

        afficherNotesContrat(contratId);

        System.out.println("\n1. Noter le véhicule");
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
            System.out.println("Erreur: " + e.getMessage());
        }
    }

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

        afficherNotesContrat(contratId);

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
            System.out.println("Erreur: " + ex.getMessage());
        }
    }

    public void afficherNotesContrat(Long contratId) {
        Contrat c = contratService.getContratById(contratId);

        System.out.println("\n=== Notes du contrat #" + c.getId() + " ===");
        System.out.println("Statut: " + c.getStatut());

        noteAffichageService.getNoteVehiculeParContrat(contratId).ifPresentOrElse(n -> {
            System.out.println("\n-- Note Véhicule --");
            System.out.println("Globale: " + n.getNoteGlobale());
            System.out.println("Commentaire: " + n.getCommentaire());
        }, () -> System.out.println("\n-- Note Véhicule -- Aucune"));

        noteAffichageService.getNoteAgentParContrat(contratId).ifPresentOrElse(n -> {
            System.out.println("\n-- Note Agent --");
            System.out.println("Globale: " + n.getNoteGlobale());
            System.out.println("Commentaire: " + n.getCommentaire());
        }, () -> System.out.println("\n-- Note Agent -- Aucune"));

        noteAffichageService.getNoteLoueurParContrat(contratId).ifPresentOrElse(n -> {
            System.out.println("\n-- Note Loueur --");
            System.out.println("Globale: " + n.getNoteGlobale());
            System.out.println("Commentaire: " + n.getCommentaire());
        }, () -> System.out.println("\n-- Note Loueur -- Aucune"));

    }

    public void menuValidationContrats(Agent agent) {
        var presignes = contratService.getContratsPresignesPourAgent(agent.getId());
        if (presignes.isEmpty()) {
            System.out.println("Aucun contrat pré-signé à valider.");
            return;
        }

        System.out.println("\n--- Contrats pré-signés à valider ---");
        for (var c : presignes) {
            System.out.println("Contrat #" + c.getId()
                    + " | Loueur=" + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom()
                    + " | Véhicule=" + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
        }

        System.out.print("ID contrat : ");
        Long id = sc.nextLong();
        sc.nextLine();

        System.out.println("1. Accepter");
        System.out.println("2. Refuser");
        int choix = sc.nextInt();
        sc.nextLine();

        try {
            if (choix == 1) {
                contratService.accepterContrat(id, agent.getId());
                System.out.println("Contrat accepté.");
            } else if (choix == 2) {
                contratService.refuserContrat(id, agent.getId());
                System.out.println("Contrat refusé.");
            } else {
                System.out.println("Choix invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public void menuMesContratsTermines(Utilisateur currentUser) {
        if (currentUser instanceof Loueur loueur) {
            afficherEtOuvrirContratTerminePourLoueur(loueur);
        } else if (currentUser instanceof Agent agent) {
            afficherEtOuvrirContratTerminePourAgent(agent);
        } else {
            System.out.println("Rôle non supporté.");
        }
    }

    private void afficherEtOuvrirContratTerminePourLoueur(Loueur loueur) {
        List<Contrat> contrats = contratService.getContratsTerminesAccepteesParLoueur(loueur.getId());
        if (contrats.isEmpty()) {
            System.out.println("Aucun contrat terminé + accepté.");
            return;
        }

        System.out.println("\n--- Mes contrats terminés (Loueur) ---");
        for (Contrat c : contrats) {
            System.out.println("Contrat #" + c.getId()
                    + " | Véhicule=" + c.getVehicule().getMarque() + " " + c.getVehicule().getModele()
                    + " | Agent=" + c.getAgent().getPrenom() + " " + c.getAgent().getNom()
                    + " | Statut=" + c.getStatut());
        }

        System.out.print("Entrer l'ID du contrat à ouvrir : ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            Contrat c = contratService.getContratTermineAcceptePourLoueur(id, loueur.getId());
            afficherDetailsContrat(c);
            afficherNotesContrat(c.getId());

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void afficherEtOuvrirContratTerminePourAgent(Agent agent) {
        List<Contrat> contrats = contratService.getContratsTerminesAccepteesParAgent(agent.getId());
        if (contrats.isEmpty()) {
            System.out.println("Aucun contrat terminé + accepté.");
            return;
        }

        System.out.println("\n--- Mes contrats terminés (Agent) ---");
        for (Contrat c : contrats) {
            System.out.println("Contrat #" + c.getId()
                    + " | Loueur=" + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom()
                    + " | Véhicule=" + c.getVehicule().getMarque() + " " + c.getVehicule().getModele()
                    + " | Statut=" + c.getStatut());
        }

        System.out.print("Entrer l'ID du contrat à ouvrir : ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            Contrat c = contratService.getContratTermineAcceptePourAgent(id, agent.getId());
            afficherDetailsContrat(c);
            afficherNotesContrat(c.getId());

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void afficherDetailsContrat(Contrat c) {
        System.out.println("\n=== Contrat #" + c.getId() + " ===");
        System.out.println("Statut   : " + c.getStatut());
        System.out.println("Début    : " + c.getDateDeb());
        System.out.println("Fin      : " + c.getDateFin());
        System.out.println("Prix     : " + c.getPrixTotal() + "€");

        if (c.getLoueur() != null)
            System.out.println("Loueur   : " + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom());

        if (c.getAgent() != null)
            System.out.println("Agent    : " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());

        if (c.getVehicule() != null)
            System.out.println("Véhicule : " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());

        System.out.println("=======================");
    }

    /**
     * Méthode partagée pour louer un véhicule (utilisée par Loueur et Agent)
     */
    public void louerVehicule(Utilisateur currentUser) {
        System.out.println("\n=== Location de Véhicule ===");

        try {
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = sc.nextInt();
            sc.nextLine();

            Vehicule vehiculeSelectionne = vehiculeService.getVehiculeDisponibleById(vehiculeId);
            if (vehiculeSelectionne == null) {
                System.out.println("Le véhicule choisi n'est pas disponible ou n'existe pas.");
                return;
            }
            System.out.println("Véhicule sélectionné: " + vehiculeSelectionne.getMarque() + " "
                    + vehiculeSelectionne.getModele() + " (ID: " + vehiculeId + ")");

            List<LocalDate> datesDisponibles = vehiculeSelectionne.getDatesDisponibles();
            System.out.println("\nDates disponibles pour ce véhicule:");
            if (datesDisponibles.isEmpty()) {
                System.out.println("Aucune date disponible pour ce véhicule.");
                return;
            }

            LocalDate dateDebut = null;
            if (datesDisponibles.size() > 1) {
                System.out.println("Nombre de dates disponibles: " + datesDisponibles.size());
                System.out.println("Première date disponible: " + datesDisponibles.get(0));
                System.out.println("Dernière date disponible: " + datesDisponibles.get(datesDisponibles.size() - 1));

                System.out.print("\nSaisissez la date de début de location (format: AAAA-MM-JJ) : ");
                String dateInput = sc.nextLine();
                try {
                    dateDebut = LocalDate.parse(dateInput);
                    if (!datesDisponibles.contains(dateDebut)) {
                        System.out.println("Cette date n'est pas disponible pour ce véhicule.");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Utilisez AAAA-MM-JJ (ex: 2026-01-15)");
                    return;
                }
            } else {
                dateDebut = datesDisponibles.get(0);
                System.out.println("Date de début: " + dateDebut);
            }

            System.out.print("Nombre de jours de location : ");
            int nbJours = sc.nextInt();
            sc.nextLine();

            System.out.println("\n=== Assurances Disponibles ===");
            List<Assurance> assurances = assuranceService.getAllAssurances();

            if (assurances.isEmpty()) {
                System.out.println("Aucune assurance disponible.");
                return;
            }

            for (int i = 0; i < assurances.size(); i++) {
                Assurance a = assurances.get(i);
                double prix = assuranceService.calculerPrix(a, nbJours);
                System.out.println((i + 1) + ". " + a.getNom() +
                        " - " + a.getPrixParJour() + "€/jour" +
                        " (Total: " + prix + "€ pour " + nbJours + " jours)");
            }

            System.out.print("\nChoisissez une assurance (numéro) : ");
            int choixAssurance = sc.nextInt();
            sc.nextLine();

            if (choixAssurance < 1 || choixAssurance > assurances.size()) {
                System.out.println("Choix invalide !");
                return;
            }

            Assurance assuranceChoisie = assurances.get(choixAssurance - 1);
            double prixAssurance = assuranceService.calculerPrix(assuranceChoisie, nbJours);

            // OPTION PARKING
            Parking parkingSelectionne = null;
            System.out.print("\nVoulez-vous choisir un parking pour le dépôt ? (O/N) : ");
            String choixParking = sc.nextLine();
            if (choixParking.equalsIgnoreCase("O")) {
                parkingSelectionne = utilisateurService.gererSelectionParkingPourLoueur();
            }

            double prixParkingOuReduction = (parkingSelectionne != null) ? parkingSelectionne.getReductionloueur() : 0;
            double prixTotal = prixAssurance - prixParkingOuReduction;

            // AFFICHAGE DES PRIX
            System.out.println("\n--- Détails du paiement ---");
            System.out.println("Prix assurance : " + prixAssurance + " euros");
            if (parkingSelectionne != null) {
                System.out.println("Réduction parking (Loueur) : -" + prixParkingOuReduction + " euros");
            }
            System.out.println("PRIX TOTAL ESTIMÉ : " + prixTotal + " euros");

            System.out.print("\nConfirmer la location ? (O/N) : ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("O")) {
                java.util.Date dateDebutContrat = java.util.Date.from(
                        dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.util.Date dateFinContrat = java.util.Date.from(
                        dateDebut.plusDays(nbJours).atStartOfDay(ZoneId.systemDefault()).toInstant());

                try {
                    Agent agentVehicule = vehiculeSelectionne.getAgent();

                    // L'utilisateur devient le loueur (Loueur ou Agent qui loue)
                    contratService.creerContratPresigne(dateDebutContrat, dateFinContrat, agentVehicule,
                            currentUser, vehiculeSelectionne, prixTotal);

                    System.out.println("\nLocation confirmée !");
                    System.out.println("Votre contrat a été créé avec succès.");
                    System.out.println("Période de location: du " + dateDebut + " au " + dateDebut.plusDays(nbJours));
                } catch (Exception e) {
                    System.out.println("Erreur lors de la création du contrat: " + e.getMessage());
                }
            } else {
                System.out.println("Location annulée.");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la location: " + e.getMessage());
            sc.nextLine();
        }
    }

}

