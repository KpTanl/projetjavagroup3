package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in).useLocale(java.util.Locale.US);
    private static UserRole currentUserRole = UserRole.Visitor;
    private Utilisateur currentUser = null;

    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final MessagerieController messagerieController;
    private final UtilisateurController utilisateurController;
    private final AgentController agentController;

    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService,
            AssuranceService assuranceService, ContratService contratService,
            MessagerieController messagerieController, UtilisateurController utilisateurController,
            AgentController agentController) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.messagerieController = messagerieController;
        this.utilisateurController = utilisateurController;
        this.agentController = agentController;
    }

    public enum UserRole {
        Visitor,
        Loueur,
        Agent,
        Exit
    }

    public void startApp() {
        while (currentUserRole != UserRole.Exit) {
            switch (currentUserRole) {
                case Visitor:
                    displayMenuVisitor();
                    break;
                case Loueur:
                    displayMenuLoueur();
                    break;
                case Agent:
                    displayMenuAgent();
                    break;
                default:
                    System.out.println("Choix invalide !");
                    break;
            }
        }
        System.out.println("Au revoir !");
    }

    private void displayMenuVisitor() {
        System.out.println("\nMenu de Visitor : ");
        System.out.println("1. Se connecter");
        System.out.println("2. Pas de compte ? S'inscrire");
        System.out.println("3. Afficher les voitures");
        System.out.println("4. Filtrer les voitures");
        System.out.println("5. Afficher les agents");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1: {
                System.out.println("Entrez votre email : ");
                String email = sc.next();
                System.out.println("Entrez votre mot de passe : ");
                String motDePasse = sc.next();
                Utilisateur utilisateur = utilisateurService.login(email, motDePasse).orElse(null);
                if (utilisateur != null) {
                    currentUser = utilisateur;
                    System.out.println("Connexion reussie !");
                    switch (utilisateur.getRole()) {
                        case Loueur:
                            currentUserRole = UserRole.Loueur;
                            break;
                        case Agent:
                            currentUserRole = UserRole.Agent;
                            break;
                    }
                } else {
                    System.out.println("Email ou mot de passe incorrect !");
                }
                break;
            }
            case 2: {
                System.out.println("--- Inscription ---");
                System.out.println("Entrez votre nom : ");
                String nom = sc.next();
                System.out.println("Entrez votre prenom : ");
                String prenom = sc.next();
                System.out.println("Entrez votre email : ");
                String userEmail = sc.next();
                System.out.println("Entrez votre mot de passe : ");
                String userMotDePasse = sc.next();

                System.out.println("Choisissez votre role (1: Loueur, 2: Agent) : ");
                int roleChoice = sc.nextInt();
                sc.nextLine();
                Utilisateur.Role role = (roleChoice == 2) ? Utilisateur.Role.Agent : Utilisateur.Role.Loueur;

                Utilisateur newUser = null;
                if (role == Utilisateur.Role.Loueur) {
                    newUser = new Loueur();
                } else {
                    newUser = new AgentParticulier();
                }

                newUser.setNom(nom);
                newUser.setPrenom(prenom);
                newUser.setEmail(userEmail);
                newUser.setMotDePasse(userMotDePasse);
                newUser.setRole(role);

                utilisateurService.register(newUser);
                System.out.println("Inscription reussie ! Vous pouvez maintenant vous connecter.");
                break;
            }
            case 3:
                vehiculeService.afficherTousLesVehicules();
                break;
            case 4:
                vehiculeService.filtrerVehicules();
                break;
            case 5:
                actionConsulterAgents();
                break;
            case 0:
                System.out.println("vos avez choisi de quitter!");
                currentUserRole = UserRole.Exit;
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
    }

    private void displayMenuLoueur() {
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
                louerVehicule();
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
            case 0:
                System.out.println("vos avez choisi de quitter!");
                currentUserRole = UserRole.Visitor;
                currentUser = null;
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
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

    private void louerVehicule() {
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

    private void displayMenuAgent() {
        if (!(currentUser instanceof Agent)) {
            System.out.println("Erreur: accès agent refusé pour cet utilisateur.");
            return;
        }
        Agent agent = (Agent) currentUser;

        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Ajouter mes vehicules");
        System.out.println("2. Supprimer mes vehicules");
        System.out.println("3. Modifier mes vehicules");
        System.out.println("4. Afficher mes vehicules");
        System.out.println("5. Filtrer les voitures");
        System.out.println("6. Messagerie");
        System.out.println("7. Gérer l'option Parking Partenaire");
        System.out.println("8. Consulter l'historique de mes véhicules");
        System.out.println("9. Valider contrats (pré-signés)");
        System.out.println("10. Noter Loueur");
        System.out.println("11. Mes contrats terminés");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                utilisateurService.ajouterVehicule(agent);
                break;
            case 2:
                utilisateurService.supprimerVehicule(agent);
                break;
            case 3:
                utilisateurService.modifierVehicule(agent);
                break;
            case 4:
                utilisateurService.afficherLesVehiculesDeAgent(agent);
                break;
            case 5:
                vehiculeService.filtrerVehicules();
                break;
            case 6:
                messagerieController.displayMenuMessagerie(currentUser);
                break;
            case 7:
                gererOptionsParkingAgent();
                break;
            case 8:
                agentController.consulterHistoriqueVehicules(currentUser);
                break;
            case 9:
                utilisateurController.menuValidationContrats(agent);
                break;
            case 10:
                utilisateurController.menuNotation(currentUser);
                break;
            case 11:
                utilisateurController.menuMesContratsTermines(currentUser);
                break;
            case 0:
                System.out.println("vous avez choisi de quitter!");
                currentUserRole = UserRole.Visitor;
                currentUser = null;
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
    }

    private void gererOptionsParkingAgent() {
        if (!(currentUser instanceof Agent)) {
            System.out.println("Erreur : Vous devez être un Agent pour accéder à cette option.");
            return;
        }

        Agent agentActuel = (Agent) currentUser;
        List<Vehicule> mesVehicules = vehiculeService.getVehiculesByAgentId(agentActuel.getId());

        if (mesVehicules == null || mesVehicules.isEmpty()) {
            System.out.println("Vous n'avez aucun véhicule enregistré.");
            return;
        }

        System.out.println("\n--- GESTION DES OPTIONS PARKING ---");
        for (int i = 0; i < mesVehicules.size(); i++) {
            Vehicule v = mesVehicules.get(i);
            System.out.println((i + 1) + ". " + v.getMarque() + " " + v.getModele()
                    + " | Option actuelle : " + v.getOptionRetour());
        }

        System.out.print("\nSélectionnez le numéro du véhicule à modifier (0 pour annuler) : ");
        int choix = sc.nextInt();
        sc.nextLine();

        if (choix > 0 && choix <= mesVehicules.size()) {
            Vehicule vSelectionne = mesVehicules.get(choix - 1);

            System.out.println("Voulez-vous activer ou désactiver l'option ?");
            System.out.println("1. Activer (retour_parking)");
            System.out.println("2. Désactiver (retour_classique)");
            int action = sc.nextInt();
            sc.nextLine();

            if (action == 1) {
                agentActuel.configurerOptionParking(vSelectionne, true);
                System.out.println("Mise à jour réussie : Option activée.");
            } else if (action == 2) {
                agentActuel.configurerOptionParking(vSelectionne, false);
                System.out.println("Mise à jour réussie : Option désactivée.");
            } else {
                System.out.println("Action annulée : choix invalide.");
            }
        }
    }

    private void actionConsulterAgents() {
        System.out.println("\n--- CONSULTATION DES AGENTS ---");
        List<Utilisateur> agents = utilisateurService.findAllAgents();

        if (agents.isEmpty()) {
            System.out.println("Désolé, aucun agent n'est inscrit pour le moment.");
            return;
        }

        for (int i = 0; i < agents.size(); i++) {
            Utilisateur a = agents.get(i);
            System.out.println((i + 1) + ". " + a.getPrenom() + " " + a.getNom() + " (Email: " + a.getEmail() + ")");
        }

        System.out.print("\nEntrez le numéro de l'agent pour voir ses véhicules (ou 0 pour annuler) : ");
        if (sc.hasNextInt()) {
            int choix = sc.nextInt();
            sc.nextLine();

            if (choix > 0 && choix <= agents.size()) {
                Utilisateur agentChoisi = agents.get(choix - 1);

                System.out.println("\n-----------------------------------------");
                System.out.println("VÉHICULES PROPOSÉS PAR " + agentChoisi.getPrenom().toUpperCase());
                System.out.println("-----------------------------------------");
                utilisateurService.afficherLesVehiculesDeAgent(agentChoisi);
                System.out.println("-----------------------------------------");
            }
        } else {
            sc.next();
            System.out.println("Saisie invalide.");
        }
    }

}
