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
import com.group3.carrental.entity.Message;
import com.group3.carrental.entity.Parking;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.service.AssuranceService;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.ParkingService;
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
    private final ParkingService parkingService ;

    @Autowired
    public AppController(UtilisateurService utilisateurService, VehiculeService vehiculeService,
            AssuranceService assuranceService, ContratService contratService, ServiceMessagerie serviceMessagerie, ParkingService parkingService) {
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.serviceMessagerie = serviceMessagerie;
        this.parkingService = parkingService;
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
        System.out.println("7. Trouver un parking partenaire");
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
                utilisateurService.louerVehicule(currentUser);
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
            case 7 :
                utilisateurService.gererSelectionParkingPourLoueur();
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

    private void displayMenuAgent() {
        if (!(currentUser instanceof Agent agent)) {
            System.out.println("Erreur: accès agent refusé pour cet utilisateur.");
            return;
        }
        System.out.println("\nMenu de Agent : ");
        System.out.println("1. Ajouter mes vehicules");
        System.out.println("2. Supprimer mes vehicules");
        System.out.println("3. Modifier mes vehicules");
        System.out.println("4. Afficher mes vehicules");
        System.out.println("5. Filtrer les voitures");
        System.out.println("6. Messagerie");
        System.out.println("7. Gérer l'option Parking Partenaire");
         System.out.println("8. Associér un vehicule à un Parking");
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
                displayMenuMessagerie();
                break;
            case 7 :
                this.gererOptionsParkingAgent();
            break;
            case 8:
                this.associerVehiculeAParking(agent); 
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
    if (!(currentUser instanceof com.group3.carrental.entity.Agent)) {
        System.out.println("Erreur : Vous devez être un Agent pour accéder à cette option.");
        return;
    }

    com.group3.carrental.entity.Agent agentActuel = (com.group3.carrental.entity.Agent) currentUser;
    
    // Récupération des véhicules via le service (données fraîches de la BDD)
    List<com.group3.carrental.entity.Vehicule> mesVehicules = vehiculeService.getVehiculesByAgentId(agentActuel.getId());

    if (mesVehicules == null || mesVehicules.isEmpty()) {
        System.out.println("Vous n'avez aucun véhicule enregistré.");
        return;
    }

    System.out.println("\n--- GESTION DES OPTIONS PARKING ---");
    for (int i = 0; i < mesVehicules.size(); i++) {
        com.group3.carrental.entity.Vehicule v = mesVehicules.get(i);
        System.out.println((i + 1) + ". " + v.getMarque() + " " + v.getModele() 
        + " | Option actuelle : " + v.getOptionRetour());
    }

    System.out.print("\nSélectionnez le numéro du véhicule à modifier (0 pour annuler) : ");
    int choix = sc.nextInt();
    sc.nextLine();

    if (choix > 0 && choix <= mesVehicules.size()) {
        com.group3.carrental.entity.Vehicule vSelectionne = mesVehicules.get(choix - 1);
        
        System.out.println("Voulez-vous activer ou désactiver l'option ?");
        System.out.println("1. Activer (retour_parking)");
        System.out.println("2. Désactiver (retour_classique)");
        int action = sc.nextInt();
        sc.nextLine();

        if (action == 1) {
            // Modification de l'objet
            agentActuel.configurerOptionParking(vSelectionne, true);
            // SAUVEGARDE DANS LA BASE DE DONNÉES
            vehiculeService.save(vSelectionne);
            System.out.println("Mise à jour réussie : Option activée et enregistrée.");
        } else if (action == 2) {
            // Modification de l'objet
            agentActuel.configurerOptionParking(vSelectionne, false);
            // SAUVEGARDE DANS LA BASE DE DONNÉES
            vehiculeService.save(vSelectionne);
            System.out.println("Mise à jour réussie : Option désactivée et enregistrée.");
        } else {
            System.out.println("Action annulée : choix invalide.");
        }
    }
}
    private void associerVehiculeAParking(Utilisateur utilisateur) {
    // Utilisation du service pour récupérer les véhicules
    List<Vehicule> mesVehicules = vehiculeService.getVehiculesByAgentId(utilisateur.getId());

    if (mesVehicules.isEmpty()) {
        System.out.println("Aucun véhicule trouvé.");
        return;
    }

    System.out.println("\n--- Sélection du véhicule ---");
    for (int i = 0; i < mesVehicules.size(); i++) {
        Vehicule v = mesVehicules.get(i);
        String statutOption = (v.getOptionRetour() == Vehicule.OptionRetour.retour_parking) ? "[OPTION ACTIVE]" : "[OPTION DESACTIVEE]";
        System.out.println((i + 1) + ". " + v.getMarque() + " " + v.getModele() + " " + statutOption);
    }

    System.out.print("Choisissez un véhicule (numéro) : ");
    int index = sc.nextInt() - 1; // On utilise 'sc'
    sc.nextLine(); 
    
    if (index < 0 || index >= mesVehicules.size()) {
        System.out.println("Choix invalide.");
        return;
    }

    Vehicule vChoisi = mesVehicules.get(index);

    // VERIFICATION : L'option doit être activée (Menu 7)
    if (vChoisi.getOptionRetour() != Vehicule.OptionRetour.retour_parking) {
        System.out.println(" Erreur : Activez d'abord l'option 'Parking Partenaire' (Option 7) pour ce véhicule !");
        return;
    }

    // LISTE DES PARKINGS
    List<Parking> parkings = parkingService.getAllParkings();
    if (parkings.isEmpty()) {
        System.out.println("Aucun parking partenaire n'est enregistré.");
        return;
    }

    System.out.println("\n--- Choisissez un parking partenaire ---");
    for (Parking p : parkings) {
        System.out.println("ID: " + p.getId() + " - " + p.getVilleP() + " (" + p.getVehiculesGares().size() + "/" + p.getNb_places_max() + " places)");
    }

    System.out.print("Entrez l'ID du parking : ");
    Long pId = sc.nextLong();
    sc.nextLine();

    Parking pChoisi = parkingService.getParkingById(pId);

    if (pChoisi != null) {
        // Appel de la méthode métier dans ton entité Parking
        if (pChoisi.ajouterVehicule(vChoisi)) {
            vehiculeService.save(vChoisi); // Persistance
            System.out.println("Succès ! Le véhicule est maintenant associé au parking de " + pChoisi.getVilleP());
            System.out.println(vChoisi.getInfosRetourParking());
        }
    } else {
        System.out.println("Parking introuvable.");
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
