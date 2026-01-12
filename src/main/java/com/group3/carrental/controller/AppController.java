package com.group3.carrental.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;
import com.group3.carrental.service.ServiceMessagerie;
import com.group3.carrental.entity.Message;
import java.util.List;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in);
    private static UserRole currentUserRole = UserRole.Visitor;
    private static Utilisateur currentUser = null;


    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;
    private final ServiceMessagerie serviceMessagerie;


    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService, ServiceMessagerie serviceMessagerie) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
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
        System.out.println("3. afficher les voitures");
        System.out.println("4. afficher les agents");
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
                    currentUser = utilisateur;
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
        System.out.println("1. Messagerie");
        System.out.println("2. Ajouter une voiture");
        System.out.println("3. Supprimer une voiture");
        System.out.println("4. Modifier une voiture");
        System.out.println("5. Afficher les voitures");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                displayMenuMessagerie();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
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
            System.out.println("3. Voir une conversation");
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


    private void displayMenuAgent() {
        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Messagerie");
        System.out.println("2. Ajouter une voiture");
        System.out.println("3. Supprimer une voiture");
        System.out.println("4. Modifier une voiture");
        System.out.println("5. Afficher les voitures");
        System.out.println("0. Quitter");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                displayMenuMessagerie();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
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

    private void menuEnvoyerMessage() {
        Integer destinataireId = choisirUtilisateurParNom();
        if (destinataireId == null) return;

        System.out.print("Contenu du message : ");
        String contenu = sc.nextLine();

        try {
            Message msg = serviceMessagerie.envoyerMessage(currentUser.getId(), destinataireId, contenu);
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
        Integer otherId = choisirUtilisateurParNom();
        if (otherId == null) return;

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


    private Integer choisirUtilisateurParNom() {
        System.out.print("Nom du destinataire : ");
        String nom = sc.nextLine().trim();

        System.out.print("Prénom (optionnel, Entrée si inconnu) : ");
        String prenom = sc.nextLine().trim();

        List<Utilisateur> candidats;
        try {
            if (prenom.isEmpty()) {
                candidats = serviceMessagerie.rechercherUtilisateursParNom(nom);
            } else {
                candidats = serviceMessagerie.rechercherUtilisateursParNomPrenom(nom, prenom);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }

        // enlever soi-même
        candidats.removeIf(u -> u.getId() == currentUser.getId());

        if (candidats.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
            return null;
        }

        System.out.println("\n--- Résultats ---");
        for (int i = 0; i < candidats.size(); i++) {
            Utilisateur u = candidats.get(i);
            System.out.println((i + 1) + ". " + u.getNom() + " " + u.getPrenom() + " (" + u.getRole() + ")");
        }

        System.out.print("Choisis un numéro (0 = annuler) : ");
        String choix = sc.nextLine().trim();

        int idx;
        try {
            idx = Integer.parseInt(choix);
        } catch (NumberFormatException e) {
            System.out.println("Choix invalide.");
            return null;
        }

        if (idx == 0) return null;
        if (idx < 1 || idx > candidats.size()) {
            System.out.println("Choix invalide.");
            return null;
        }

        return candidats.get(idx - 1).getId();
    }


}
