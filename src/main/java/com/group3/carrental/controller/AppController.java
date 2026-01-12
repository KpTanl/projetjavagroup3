package com.group3.carrental.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.entity.Message;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;
import com.group3.carrental.service.ServiceMessagerie;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in);
    private static UserRole currentUserRole = UserRole.Visitor;
    private Utilisateur currentUser = null; // Utilisateur connecté

    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final ServiceMessagerie serviceMessagerie;

    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService,
            AssuranceService assuranceService, ContratService contratService, ServiceMessagerie serviceMessagerie) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.serviceMessagerie = serviceMessagerie;
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
                    // Par défaut un Agent Particulier si pas précisé
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
                // TODO: Afficher les agents
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
                displayMenuMessagerie();
                break;
            case 6:
                // TODO: Mon profil
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

    /**
     * Afficher toutes les assurances disponibles
     */
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

    /**
     * Processus de location d'un véhicule
     * Étapes: Choisir véhicule -> Dates -> Assurance -> Validation
     */
    private void louerVehicule() {
        System.out.println("\n=== Location de Véhicule ===");

        try {
            // Étape 1: Afficher et choisir un véhicule (uniquement les non loués)
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = sc.nextInt();
            sc.nextLine(); // Consommer retour ligne

            // Vérifier que le véhicule sélectionné est disponible (Non_loué)
            com.group3.carrental.entity.Vehicule vehiculeSelectionne = vehiculeService
                    .getVehiculeDisponibleById(vehiculeId);
            if (vehiculeSelectionne == null) {
                System.out.println("Le véhicule choisi n'est pas disponible (non loué) ou n'existe pas.");
                return;
            }
            System.out.println("Véhicule sélectionné: " + vehiculeSelectionne.getMarque() + " "
                    + vehiculeSelectionne.getModele() + " (ID: " + vehiculeId + ")");

            // Étape 2: Choisir les dates
            System.out.print("Nombre de jours de location : ");
            int nbJours = sc.nextInt();
            sc.nextLine();

            // Étape 3: Afficher et choisir une assurance
            System.out.println("\n=== Assurances Disponibles ===");
            List<Assurance> assurances = assuranceService.getAllAssurances();

            if (assurances.isEmpty()) {
                System.out.println("Aucune assurance disponible.");
                return;
            }

            // Afficher les assurances avec prix
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

            // Étape 4: Récapitulatif et validation
            System.out.println("\n=== Récapitulatif de Location ===");
            System.out.println("Véhicule: ID " + vehiculeId);
            System.out.println("Durée: " + nbJours + " jours");
            System.out.println("Assurance: " + assuranceChoisie.getNom());
            System.out.println("Prix assurance: " + prixAssurance + "€");
            System.out.println("\nPrix total estimé: " + prixAssurance + "€");

            System.out.print("\nConfirmer la location ? (O/N) : ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("O")) {
                // Créer le contrat dans la base de données
                java.util.Date today = new java.util.Date();
                java.util.Date endDate = new java.util.Date(today.getTime() + (long) nbJours * 24 * 60 * 60 * 1000);

                try {
                    contratService.creerContrat(today, endDate, null, null, null, prixAssurance);

                    System.out.println("\nLocation confirmée !");
                    System.out.println("Votre contrat a été créé avec succès.");
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
        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Ajouter mes vehicules");
        System.out.println("2. Supprimer mes vehicules");
        System.out.println("3. Modifier mes vehicules");
        System.out.println("4. Afficher mes vehicules");
        System.out.println("5. Filtrer les voitures");
        System.out.println("6. Messagerie");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                utilisateurService.ajouterVehicule(currentUser);
                break;
            case 2:
                utilisateurService.supprimerVehicule(currentUser);
                break;
            case 3:
                utilisateurService.modifierVehicule(currentUser);
                break;
            case 4:
                utilisateurService.afficherLesVehiculesDeAgent(currentUser);
                break;
            case 5:
                vehiculeService.filtrerVehicules();
                break;
            case 6:
                displayMenuMessagerie();
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

    // ========== Messagerie ==========
    private void displayMenuMessagerie() {
        if (currentUser == null) {
            System.out.println("Accès refusé : vous devez être connecté.");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Messagerie ---");
            System.out.println("1. Envoyer un message");
            System.out.println("2. Voir ma boîte de réception");
            System.out.println("3. Voir une conversation (par id)");
            System.out.println("0. Retour");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    menuEnvoyerMessage();
                    break;
                case 2:
                    menuAfficherInbox();
                    break;
                case 3:
                    menuAfficherConversation();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void menuEnvoyerMessage() {
        System.out.print("ID du destinataire : ");
        int destinataireId = Integer.parseInt(sc.nextLine());

        System.out.print("Contenu du message : ");
        String contenu = sc.nextLine();

        try {
            Message msg = serviceMessagerie.envoyerMessage(
                    currentUser.getId(),
                    destinataireId,
                    contenu);
            System.out.println("Message envoyé ! (id=" + msg.getId() + ", date=" + msg.getDateEnvoi() + ")");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void menuAfficherInbox() {
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

    private void menuAfficherConversation() {
        System.out.print("ID de l'utilisateur : ");
        int otherId = Integer.parseInt(sc.nextLine());

        try {
            List<Message> conv = serviceMessagerie.consulterConversation(currentUser.getId(), otherId);
            if (conv.isEmpty()) {
                System.out.println("(Aucun message entre vous)");
                return;
            }

            System.out.println("\n--- Conversation ---");
            for (Message m : conv) {
                String who = (m.getExpediteur().getId() == currentUser.getId()) ? "Moi" : m.getExpediteur().getPrenom();
                System.out.println("[" + m.getDateEnvoi() + "] " + who + " : " + m.getContenu());
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
