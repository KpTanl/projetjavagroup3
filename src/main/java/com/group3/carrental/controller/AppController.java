package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Message;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;
import com.group3.carrental.service.ServiceMessagerie;

@Component
public class AppController {
    private static final Scanner sc = new Scanner(System.in);
    private static UserRole currentUserRole = UserRole.Visitor;
    private Utilisateur currentUser = null;

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
                afficherMonProfil();
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
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = sc.nextInt();
            sc.nextLine();

            com.group3.carrental.entity.Vehicule vehiculeSelectionne = vehiculeService
                    .getVehiculeDisponibleById(vehiculeId);
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
                    // Récupérer l'agent du véhicule et le loueur courant
                    com.group3.carrental.entity.Agent agentVehicule = vehiculeSelectionne.getAgent();
                    com.group3.carrental.entity.Loueur loueurCourant = null;
                    if (currentUser instanceof com.group3.carrental.entity.Loueur) {
                        loueurCourant = (com.group3.carrental.entity.Loueur) currentUser;
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

    // ========== Mon profil (Loueur) ==========
    private void afficherMonProfil() {
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
                    afficherHistoriqueLocations();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void afficherHistoriqueLocations() {
        if (currentUser == null) {
            System.out.println("Accès refusé : vous devez être connecté.");
            return;
        }

        List<Contrat> contrats = contratService.getContratsParLoueur(currentUser.getId());
        if (contrats.isEmpty()) {
            System.out.println("Aucune location trouvée.");
            return;
        }

        System.out.println("\n--- Historique des locations ---");
        for (Contrat c : contrats) {
            LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Contrat #" + c.getId() + " : du " + deb + " au " + fin);

            if (c.getVehicule() != null) {
                System.out.println("  Véhicule : " + c.getVehicule().getMarque() + " " + c.getVehicule().getModele());
                System.out.println("  Lieu     : " + c.getVehicule().getLocalisationComplete());
            }

            if (c.getAgent() != null) {
                System.out.println("  Agent    : " + c.getAgent().getPrenom() + " " + c.getAgent().getNom());
            }
            System.out.println("  Prix total : " + c.getPrixTotal() + "€");
            System.out.println("------------------------------------");
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

    private void menuEnvoyerMessage() {
        Integer destinataireId = choisirUtilisateurParNom();
        if (destinataireId == null)
            return;

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
        Integer otherId = choisirUtilisateurParNom();
        if (otherId == null)
            return;

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

    // 来自 jihane 分支的新功能 - 按名字搜索用户
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

        if (idx == 0)
            return null;
        if (idx < 1 || idx > candidats.size()) {
            System.out.println("Choix invalide.");
            return null;
        }

        return candidats.get(idx - 1).getId();
    }
}
