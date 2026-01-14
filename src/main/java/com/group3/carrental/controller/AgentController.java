package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;
import com.group3.carrental.service.OptionService;

@Component
public class AgentController {
    private static final Scanner sc = new Scanner(System.in);
    private final VehiculeService vehiculeService;
    private final ContratService contratService;
    private final UtilisateurService utilisateurService;
    private final MessagerieController messagerieController;
    private final UtilisateurController utilisateurController;
    private final OptionService optionService;

    @Autowired
    public AgentController(VehiculeService vehiculeService, ContratService contratService,
            UtilisateurService utilisateurService, MessagerieController messagerieController,
            @Lazy UtilisateurController utilisateurController, OptionService optionService) {
        this.vehiculeService = vehiculeService;
        this.contratService = contratService;
        this.utilisateurService = utilisateurService;
        this.messagerieController = messagerieController;
        this.utilisateurController = utilisateurController;
        this.optionService = optionService;
    }

    /**
     * Affiche le menu Agent et gère les choix.
     * 
     * @param currentUser l'utilisateur courant
     * @return le nouveau rôle après l'action (null si déconnexion)
     */
    public Utilisateur displayMenuAgent(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent)) {
            System.out.println("Erreur: accès agent refusé pour cet utilisateur.");
            return currentUser;
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
        System.out.println("9. Gérer option signature manuelle (contrats)");
        System.out.println("10. Valider contrats (pré-signés)");
        System.out.println("11. Noter Loueur");
        System.out.println("12. Droit de réponse (discussion sur notes)");
        System.out.println("13. Mes contrats terminés");
        System.out.println("14. Mes contrats et PDF");
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
                gererOptionsParkingAgent(currentUser);
                break;
            case 8:
                consulterHistoriqueVehicules(currentUser);
                break;
            case 9:
                menuOptionSignatureManuelle(agent);
                break;
            case 10:
                boolean optionManuelle = optionService.hasActiveOption(
                        agent.getId(),
                        OptionService.OPT_SIGNATURE_MANUELLE
                );
                if (!optionManuelle) {
                    System.out.println("Option non active : les contrats sont acceptés automatiquement à la création.");
                    break;
                }
                utilisateurController.menuValidationContrats(agent);
                break;
            case 11:
                utilisateurController.menuNotation(currentUser);
                break;
            case 12:
                utilisateurController.menuDiscussionNotes(currentUser);
                break;
            case 13:
                utilisateurController.menuMesContratsTermines(currentUser);
                break;
            case 14:
                afficherMesContrats(currentUser);
                break;
            case 0:
                System.out.println("vous avez choisi de quitter!");
                return null; // Signal déconnexion
            default:
                System.out.println("Choix invalide !");
                break;
        }
        return currentUser;
    }

    /**
     * Gérer les options de parking pour les véhicules de l'agent.
     */
    public void gererOptionsParkingAgent(Utilisateur currentUser) {
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

    /**
     * Consulter l'historique des locations pour les véhicules de l'agent.
     * L'agent ne peut consulter que ses propres véhicules.
     */
    public void consulterHistoriqueVehicules(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent)) {
            System.out.println("Accès refusé : vous devez être un Agent.");
            return;
        }

        // Afficher les véhicules de l'agent
        System.out.println("\n=== Mes Véhicules ===");
        List<Vehicule> mesVehicules = vehiculeService.getVehiculesByAgentId(currentUser.getId());

        if (mesVehicules.isEmpty()) {
            System.out.println("Vous n'avez aucun véhicule.");
            return;
        }

        for (Vehicule v : mesVehicules) {
            System.out.println(v.getId() + ". " + v.getMarque() + " " + v.getModele() + " (" + v.getCouleur() + ")");
        }

        System.out.print("\nEntrez l'ID du véhicule pour voir son historique : ");
        int vehiculeId = sc.nextInt();
        sc.nextLine();

        // Vérifier que le véhicule appartient à l'agent
        Vehicule vehiculeChoisi = mesVehicules.stream()
                .filter(v -> v.getId() == vehiculeId)
                .findFirst()
                .orElse(null);

        if (vehiculeChoisi == null) {
            System.out.println("Ce véhicule ne vous appartient pas ou n'existe pas.");
            return;
        }

        // Récupérer l'historique des contrats pour ce véhicule
        List<Contrat> contrats = contratService.getHistoriqueVehiculedeAgent(currentUser.getId(), vehiculeId);

        if (contrats.isEmpty()) {
            System.out.println("\nAucun historique de location pour ce véhicule.");
            return;
        }

        System.out.println("\n=== Historique du véhicule : " + vehiculeChoisi.getMarque() + " "
                + vehiculeChoisi.getModele() + " ===");
        for (Contrat c : contrats) {
            LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("\nContrat #" + c.getId());
            System.out.println("  Période    : du " + deb + " au " + fin);
            if (c.getLoueur() != null) {
                System.out.println("  Loueur     : " + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom());
            }
            System.out.println("  Prix total : " + c.getPrixTotal() + "€");
            System.out.println("  Statut     : " + (c.getStatut() != null ? c.getStatut() : "Non défini"));
            System.out.println("------------------------------------");
        }
    }

    /**
     * Afficher les contrats de l'agent.
     */
    private void afficherMesContrats(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent agent)) {
            return;
        }
        List<Contrat> tousContrats = contratService.getTousLesContrats();
        List<Contrat> contratsAgent = tousContrats.stream()
                .filter(c -> c.getAgent() != null && c.getAgent().getId() == agent.getId())
                .toList();
        afficherListeContrats(contratsAgent, currentUser);
    }

    private void afficherListeContrats(List<Contrat> contrats, Utilisateur currentUser) {
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
            System.out.println("Loueur: " + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom());
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
    private void menuOptionSignatureManuelle(Agent agent) {
        final float PRIX_MENSUEL = 9.99f; // mets le prix demandé dans l'énoncé si vous en avez un

        boolean active = optionService.hasActiveOption(agent.getId(), OptionService.OPT_SIGNATURE_MANUELLE);

        System.out.println("\n=== Option: Signature manuelle des contrats ===");
        System.out.println("Statut actuel : " + (active ? "ACTIVE" : "INACTIVE"));
        System.out.println("1. " + (active ? "Désactiver" : "Activer"));
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");

        int choix = sc.nextInt();
        sc.nextLine();

        try {
            if (choix == 1) {
                optionService.toggleOption(agent, OptionService.OPT_SIGNATURE_MANUELLE, PRIX_MENSUEL);
                boolean newState = optionService.hasActiveOption(agent.getId(), OptionService.OPT_SIGNATURE_MANUELLE);
                System.out.println("Nouveau statut : " + (newState ? "ACTIVE" : "INACTIVE"));
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}
