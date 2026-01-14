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
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.VehiculeService;

@Component
public class LoueurController {
    private static final Scanner sc = new Scanner(System.in);
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final MessagerieController messagerieController;
    private final UtilisateurController utilisateurController;

    @Autowired
    public LoueurController(VehiculeService vehiculeService, AssuranceService assuranceService,
            ContratService contratService, MessagerieController messagerieController,
            UtilisateurController utilisateurController) {
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.messagerieController = messagerieController;
        this.utilisateurController = utilisateurController;
    }

    /**
     * Affiche le menu Loueur et gère les choix.
     * 
     * @param currentUser l'utilisateur courant
     * @return null si déconnexion, sinon currentUser
     */
    public Utilisateur displayMenuLoueur(Utilisateur currentUser) {
        // Vérifier s'il y a des véhicules à rendre
        if (currentUser instanceof Loueur loueur) {
            List<Contrat> contratsARendre = contratService.getContratsARendre(loueur.getId());
            if (!contratsARendre.isEmpty()) {
                afficherVehiculesARendre(contratsARendre);
            }
        }

        System.out.println("\nMenu de Loueur : ");
        System.out.println("1. Consulter les véhicules");
        System.out.println("2. Filtrer les voitures");
        System.out.println("3. Louer un véhicule");
        System.out.println("4. Consulter les assurances");
        System.out.println("5. Messagerie");
        System.out.println("6. Mon profil");
        System.out.println("7. Suggestions autour de chez moi (Rayon X km)");
        System.out.println("8. Noter");
        System.out.println("9. Mes contrats terminés");
        System.out.println("10. Mes contrats et PDF");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                vehiculeService.afficherTousLesVehicules();
                break;
            case 2:
                vehiculeService.filtrerVehicules();
                break;
            case 3:
                louerVehicule(currentUser);
                break;
            case 4:
                afficherAssurances();
                break;
            case 5:
                messagerieController.displayMenuMessagerie(currentUser);
                break;
            case 6:
                utilisateurController.afficherMonProfil(currentUser);
                break;
            case 7:
                System.out.print("Entrez le rayon maximum souhaité (en km) : ");
                double rayonsuggestion = sc.nextDouble();
                sc.nextLine();
                vehiculeService.suggererVehiculesProches(currentUser, rayonsuggestion);
                break;
            case 8:
                utilisateurController.menuNotation(currentUser);
                break;
            case 9:
                utilisateurController.menuMesContratsTermines(currentUser);
                break;
            case 10:
                afficherMesContrats(currentUser);
                break;
            case 0:
                System.out.println("vos avez choisi de quitter!");
                return null; // Signal déconnexion
            default:
                System.out.println("Choix invalide !");
                break;
        }
        return currentUser;
    }

    private void afficherAssurances() {
        System.out.println("\n=== Assurances Disponibles ===");
        List<Assurance> assurances = assuranceService.getAllAssurances();

        if (assurances.isEmpty()) {
            System.out.println("Aucune assurance disponible.");
            return;
        }

        for (Assurance assurance : assurances) {
            System.out.println("\n- " + assurance.getNom());
            System.out.println("  Prix: " + assurance.getPrixParJour() + "€/jour");
            System.out.println("  Couverture: " + assurance.getGrilleTarifaire());
        }

        System.out.println("\nExemple pour 5 jours:");
        for (Assurance assurance : assurances) {
            double prix5jours = assuranceService.calculerPrix(assurance, 5);
            System.out.println("- " + assurance.getNom() + ": " + prix5jours + "€");
        }
    }

    private void louerVehicule(Utilisateur currentUser) {
        System.out.println("\n=== Location de Véhicule ===");

        try {
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = sc.nextInt();
            sc.nextLine();

            Vehicule vehiculeSelectionne = vehiculeService.getVehiculeDisponibleById(vehiculeId);
            if (vehiculeSelectionne == null) {
                System.out.println("Le véhicule choisi n'est pas disponible (non loué) ou n'existe pas.");
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

            System.out.println("\n=== Récapitulatif de Location ===");
            System.out.println("Véhicule: ID " + vehiculeId);
            System.out.println("Date de début: " + dateDebut);
            System.out.println("Durée: " + nbJours + " jours");
            System.out.println("Assurance: " + assuranceChoisie.getNom());
            System.out.println("Prix assurance: " + prixAssurance + "€");
            System.out.println("\nPrix total estimé: " + prixAssurance + "€");

            System.out.print("\nConfirmer la location ? (O/N) : ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("O")) {
                java.util.Date dateDebutContrat = java.util.Date.from(
                        dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.util.Date dateFinContrat = java.util.Date.from(
                        dateDebut.plusDays(nbJours).atStartOfDay(ZoneId.systemDefault()).toInstant());

                try {
                    Agent agentVehicule = vehiculeSelectionne.getAgent();
                    Loueur loueurCourant = null;
                    if (currentUser instanceof Loueur) {
                        loueurCourant = (Loueur) currentUser;
                    }

                    contratService.creerContrat(dateDebutContrat, dateFinContrat, agentVehicule, loueurCourant,
                            vehiculeSelectionne, prixAssurance);

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

    private void afficherVehiculesARendre(List<Contrat> contratsARendre) {
        System.out.println("\n========================================");
        System.out.println("    VEHICULES A RENDRE");
        System.out.println("========================================");

        for (int i = 0; i < contratsARendre.size(); i++) {
            Contrat c = contratsARendre.get(i);
            System.out.println("\n--- Véhicule " + (i + 1) + " ---");
            System.out.println("Contrat ID: " + c.getId());
            System.out.println("Véhicule: " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
            System.out.println("Couleur: " + c.getVehicule().getCouleur());
            System.out.println("Date de fin: " + c.getDateFin());
            System.out.println("Agent: " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
        }

        System.out.println("\n1. Rendre un véhicule");
        System.out.println("0. Continuer vers le menu");
        System.out.print("Votre choix: ");
        int choix = sc.nextInt();
        sc.nextLine();

        if (choix == 1) {
            rendreVehicule(contratsARendre);
        }
    }

    private void rendreVehicule(List<Contrat> contratsARendre) {
        if (contratsARendre.size() == 1) {
            traiterRetourVehicule(contratsARendre.get(0));
        } else {
            System.out.print("Numéro du véhicule à rendre (1-" + contratsARendre.size() + "): ");
            int num = sc.nextInt();
            sc.nextLine();

            if (num < 1 || num > contratsARendre.size()) {
                System.out.println("Choix invalide.");
                return;
            }

            traiterRetourVehicule(contratsARendre.get(num - 1));
        }
    }

    private void traiterRetourVehicule(Contrat contrat) {
        System.out.println("\n=== Retour du véhicule: " + contrat.getVehicule().getMarque() + " "
                + contrat.getVehicule().getModele() + " ===");

        Vehicule vehicule = contrat.getVehicule();

        // Kilométrage
        System.out.println("\n--- Kilométrage ---");
        System.out.println("Kilométrage actuel enregistré: " + vehicule.getKilometrage() + " km");
        System.out.print("Nouveau kilométrage du véhicule (en km): ");
        int nouveauKilometrage = sc.nextInt();
        sc.nextLine();

        if (nouveauKilometrage < vehicule.getKilometrage()) {
            System.out.println("Attention: Le nouveau kilométrage est inférieur au précédent!");
            System.out.print("Confirmez-vous? (oui/non): ");
            String confirmation = sc.nextLine().toLowerCase();
            if (!confirmation.equals("oui")) {
                System.out.println("Opération annulée.");
                return;
            }
        }

        int distanceParcourue = nouveauKilometrage - vehicule.getKilometrage();
        System.out.println("Distance parcourue: " + distanceParcourue + " km");

        // Photo de kilométrage
        System.out.println("\n--- Photo du kilométrage ---");
        System.out.println("Veuillez saisir le chemin du fichier photo (ex: C:/photos/kilometrage.jpg)");
        System.out.print("Chemin: ");
        String cheminPhoto = sc.nextLine();

        if (cheminPhoto.isEmpty()) {
            cheminPhoto = "Non fourni";
        }

        // Mettre à jour le kilométrage
        vehicule.setKilometrage(nouveauKilometrage);
        vehiculeService.save(vehicule);

        // Marquer le contrat comme rendu
        contratService.rendreVehicule(contrat.getId(), cheminPhoto);

        System.out.println("\n========================================");
        System.out.println("  VEHICULE RENDU AVEC SUCCES !");
        System.out.println("========================================");
        System.out.println("Kilométrage enregistré: " + nouveauKilometrage + " km");
        System.out.println("Photo enregistrée: " + cheminPhoto);
    }

    private void afficherMesContrats(Utilisateur currentUser) {
        if (!(currentUser instanceof Loueur loueur)) {
            return;
        }
        List<Contrat> contrats = contratService.getContratsParLoueur(loueur.getId());
        afficherListeContrats(contrats);
    }

    private void afficherListeContrats(List<Contrat> contrats) {
        if (contrats.isEmpty()) {
            System.out.println("\nAucun contrat trouvé.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("         MES CONTRATS");
        System.out.println("========================================");

        for (int i = 0; i < contrats.size(); i++) {
            Contrat c = contrats.get(i);
            System.out.println("\n--- Contrat " + (i + 1) + " ---");
            System.out.println("ID: " + c.getId());
            System.out.println("Véhicule: " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
            System.out.println("Du " + c.getDateDeb() + " au " + c.getDateFin());
            System.out.println("Prix total: " + c.getPrixTotal() + " EUR");
            System.out.println("Statut: " + (c.getStatut() != null ? c.getStatut() : "Non défini"));
            System.out.println("Agent: " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
        }

        System.out.println("\n1. Télécharger le PDF d'un contrat");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        int choix = sc.nextInt();
        sc.nextLine();

        if (choix == 1) {
            System.out.print("Numéro du contrat (1-" + contrats.size() + "): ");
            int num = sc.nextInt();
            sc.nextLine();

            if (num < 1 || num > contrats.size()) {
                System.out.println("Choix invalide.");
                return;
            }

            Contrat contratSelectionne = contrats.get(num - 1);

            System.out.print("Chemin du dossier de destination (ex: C:/contrats): ");
            String dossier = sc.nextLine();

            if (dossier.isEmpty()) {
                dossier = System.getProperty("user.home") + "/Downloads";
                System.out.println("Utilisation du dossier par défaut: " + dossier);
            }

            String cheminPdf = contratService.genererPdfContrat(contratSelectionne.getId(), dossier);
            if (cheminPdf != null) {
                System.out.println("\nPDF généré avec succès!");
                System.out.println("Emplacement: " + cheminPdf);
            } else {
                System.out.println("\nErreur lors de la génération du PDF.");
            }
        }
    }

}