package com.group3.carrental.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in);
    private static UserRole currentUserRole = UserRole.Visitor;

    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;
    private final AssuranceService assuranceService;
    private final ContratService contratService;

    /**
     * Constructeur avec injection de dépendances.
     * Spring injecte automatiquement les services nécessaires.
     */
    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService,
                        AssuranceService assuranceService, ContratService contratService) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
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
        System.out.println("3. Consulter les véhicules");
        System.out.println("4. Consulter les agents");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        switch (choice) {
            case 1: {
                System.out.println("Entrez votre email : ");
                String email = sc.next();
                System.out.println("Entrez votre mot de passe : ");
                String motDePasse = sc.next();
                Utilisateur utilisateur = utilisateurService.login(email, motDePasse).orElse(null);
                if (utilisateur != null) {
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
                Utilisateur.Role role = (roleChoice == 2) ? Utilisateur.Role.Agent : Utilisateur.Role.Loueur;

                Utilisateur newUser = new Utilisateur();
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
                afficherAssurances();
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
        System.out.println("2. Consulter les agents");
        System.out.println("3. Louer un véhicule");
        System.out.println("4. Messagerie");
        System.out.println("5. Mon profil");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                vehiculeService.afficherTousLesVehicules();
                break;
            case 2:
                break;
            case 3:
                louerVehicule();
                break;
            case 4:
                break;
            case 5:
                break;
            case 0:
                System.out.println("vos avez choisi de quitter!");
                currentUserRole = UserRole.Visitor;
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
            // Étape 1: Afficher et choisir un véhicule
            vehiculeService.afficherTousLesVehicules();
            System.out.print("\nEntrez l'ID du véhicule à louer : ");
            int vehiculeId = sc.nextInt();
            sc.nextLine(); // Consommer retour ligne
            
            // Récupérer le véhicule (pour l'instant on simule, à améliorer)
            System.out.println("Véhicule sélectionné (ID: " + vehiculeId + ")");
            
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
                
                // TODO: Récupérer le loueur et le véhicule actuels depuis la session utilisateur
                // Pour l'instant, on utilise des valeurs par défaut
                try {
                    // Récupérer le véhicule (simulation - à améliorer)
                    // Récupérer le loueur actuel (simulation - à améliorer)
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
        System.out.println("1. Ajouter une voiture");
        System.out.println("2. Supprimer une voiture");
        System.out.println("3. Modifier une voiture");
        System.out.println("4. Afficher les voitures");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 0:
                System.out.println("vos avez choisi de quitter!");
                currentUserRole = UserRole.Visitor;
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
    }
}
