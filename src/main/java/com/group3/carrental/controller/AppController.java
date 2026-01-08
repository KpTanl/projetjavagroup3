package com.group3.carrental.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in);
    private static UserRole currentUserRole = UserRole.Visitor;
    private Utilisateur currentUser = null; // Utilisateur connecté

    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;

    /**
     * Constructeur avec injection de dépendances.
     * Spring injecte automatiquement les services nécessaires.
     */
    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
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
        switch (choice) {
            case 1: {
                System.out.println("Entrez votre email : ");
                String email = sc.next();
                System.out.println("Entrez votre mot de passe : ");
                String motDePasse = sc.next();
                Utilisateur utilisateur = utilisateurService.login(email, motDePasse).orElse(null);
                if (utilisateur != null) {
                    currentUser = utilisateur; // Sauvegarder l'utilisateur
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
                vehiculeService.filtrerVehicules();
                break;
            case 5:
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
        System.out.println("1. Ajouter une voiture");
        System.out.println("2. Supprimer une voiture");
        System.out.println("3. Modifier une voiture");
        System.out.println("4. Afficher les voitures");
        System.out.println("5. Filtrer les voitures");
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
                vehiculeService.afficherTousLesVehicules();
                break;
            case 5:
                vehiculeService.filtrerVehicules();
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

    private void displayMenuAgent() {
        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Ajouter mes vehicules");
        System.out.println("2. Supprimer mes vehicules");
        System.out.println("3. Modifier mes vehicules");
        System.out.println("4. Afficher mes vehicules");
        System.out.println("5. Filtrer les voitures");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
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
