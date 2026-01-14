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
import com.group3.carrental.entity.Parking;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class LoueurController {
    private static final Scanner sc = new Scanner(System.in);
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final MessagerieController messagerieController;
    private final UtilisateurController utilisateurController;
    private final UtilisateurService utilisateurService;

    @Autowired
    public LoueurController(VehiculeService vehiculeService, AssuranceService assuranceService,
            ContratService contratService, MessagerieController messagerieController,
            UtilisateurController utilisateurController, UtilisateurService utilisateurService) {
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.messagerieController = messagerieController;
        this.utilisateurController = utilisateurController;
        this.utilisateurService = utilisateurService;
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
        System.out.println("11. Trouver un parking partenaire");
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
                utilisateurController.louerVehicule(currentUser);
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
            case 11:
                utilisateurService.gererSelectionParkingPourLoueur();
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

        // Parrainage: Récompenser le parrain si c'est la première location terminée
        Utilisateur loueur = contrat.getLoueur();
        if (loueur instanceof Loueur) {
            Loueur loueurCast = (Loueur) loueur;
            if (loueurCast.getParrain() != null && !loueurCast.isBonusParrainageRecu()) {
                Utilisateur parrain = loueurCast.getParrain();
                double bonus = 50.0;
                parrain.setSoldePorteMonnaie(parrain.getSoldePorteMonnaie() + bonus);
                loueurCast.setBonusParrainageRecu(true);
                utilisateurService.mettreAJour(parrain);
                utilisateurService.mettreAJour(loueurCast);
                System.out.println("\n*** PARRAINAGE ***");
                System.out.println("Félicitations ! Votre parrain " + parrain.getPrenom() + " " + parrain.getNom()
                    + " a reçu " + bonus + "€ de crédit !");
            }
        }

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