package com.group3.carrental.controller;

import java.util.Scanner;

import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.entity.Utilisateur;

public class AppController {
    private static final Scanner sc = new Scanner(System.in); // Scanner unique pour toute l'application
    private static UserRole currentUserRole = UserRole.Visitor;
    private static UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    public enum UserRole {
        Visitor,
        Loueur,
        Agent,
        Exit
    }

    public static void startApp() {

        while (currentUserRole != UserRole.Exit) {
            switch (currentUserRole) {
                case Visitor:
                    displayMenuVisitor();
                    break;
                case Loueur:
                    break;
                case Agent:
                    break;
                case Exit:
                    break;
                default:
                    System.out.println("Choix invalide !");
                    break;
            }

        }
        System.out.println("Au revoir !");

    }

    private static void displayMenuVisitor() {
        System.out.println("\nMenu de Visitor : ");
        System.out.println("1. Se connecter"); // fini
        System.out.println("2. Pas de compte ? S'inscrire"); // pas fini
        System.out.println("3. afficher les voitures"); // pas fini
        System.out.println("4. afficher les agents"); // pas fini
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Entrez votre email : ");
                String email = sc.next();
                System.out.println("Entrez votre mot de passe : ");
                String motDePasse = sc.next();
                Utilisateur utilisateur = utilisateurRepository.connecter(email, motDePasse);
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
            case 2:

                break;
            case 3:
                break;
            case 4:
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

    private static void displayMenuLoueur() { // pas finir
        System.out.println("\nMenu de Loueur : ");
        System.out.println("1. Ajouter une voiture"); // pas fini
        System.out.println("2. Supprimer une voiture"); // pas fini
        System.out.println("3. Modifier une voiture"); // pas fini
        System.out.println("4. Afficher les voitures"); // pas fini
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

    private static void displayMenuAgent() { // pas finir
        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Ajouter une voiture"); // pas fini
        System.out.println("2. Supprimer une voiture"); // pas fini
        System.out.println("3. Modifier une voiture"); // pas fini
        System.out.println("4. Afficher les voitures"); // pas fini
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
