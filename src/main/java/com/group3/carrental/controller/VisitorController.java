package com.group3.carrental.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;

@Component
public class VisitorController {
    private static final Scanner sc = new Scanner(System.in);
    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;

    @Autowired
    public VisitorController(UtilisateurService utilisateurService, VehiculeService vehiculeService) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
    }

    /**
     * Résultat du menu Visitor.
     */
    public static class VisitorResult {
        public Utilisateur user;
        public boolean exit;

        public VisitorResult(Utilisateur user, boolean exit) {
            this.user = user;
            this.exit = exit;
        }
    }

    /**
     * Affiche le menu Visitor et gère les choix.
     * 
     * @return VisitorResult contenant l'utilisateur connecté (ou null) et si on
     *         quitte l'app
     */
    public VisitorResult displayMenuVisitor() {
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
                    System.out.println("Connexion reussie !");
                    return new VisitorResult(utilisateur, false);
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

                // Parrainage (Referral)
                System.out.println("Email du parrain (laisser vide si aucun) : ");
                String emailParrain = sc.nextLine().trim();
                if (!emailParrain.isEmpty()) {
                    Utilisateur parrain = utilisateurService.findByEmail(emailParrain);
                    if (parrain != null) {
                        newUser.setParrain(parrain);
                        System.out.println("Parrain trouvé : " + parrain.getPrenom() + " " + parrain.getNom());
                    } else {
                        System.out.println("Aucun utilisateur trouvé avec cet email. Inscription sans parrain.");
                    }
                }

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
                utilisateurService.consulterAgents();
                break;
            case 0:
                System.out.println("vos avez choisi de quitter!");
                return new VisitorResult(null, true);
            default:
                System.out.println("Choix invalide !");
                break;
        }
        return new VisitorResult(null, false);
    }
}
