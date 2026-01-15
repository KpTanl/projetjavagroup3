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
import com.group3.carrental.entity.OptionPayanteAgent;
import com.group3.carrental.entity.PrestataireEntretien;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.UtilisateurService;
import com.group3.carrental.service.VehiculeService;
import com.group3.carrental.service.OptionService;
import com.group3.carrental.repository.PrestataireEntretienRepository;
import com.group3.carrental.repository.UtilisateurRepository;

@Component
public class AgentController {
    private static final Scanner sc = new Scanner(System.in);
    private final VehiculeService vehiculeService;
    private final ContratService contratService;
    private final UtilisateurService utilisateurService;
    private final MessagerieController messagerieController;
    private final UtilisateurController utilisateurController;
    private final OptionService optionService;
    private final PrestataireEntretienRepository prestataireRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public AgentController(VehiculeService vehiculeService, ContratService contratService,
            UtilisateurService utilisateurService, MessagerieController messagerieController,
            @Lazy UtilisateurController utilisateurController, OptionService optionService,
            PrestataireEntretienRepository prestataireRepository, UtilisateurRepository utilisateurRepository) {
        this.vehiculeService = vehiculeService;
        this.contratService = contratService;
        this.utilisateurService = utilisateurService;
        this.messagerieController = messagerieController;
        this.utilisateurController = utilisateurController;
        this.optionService = optionService;
        this.prestataireRepository = prestataireRepository;
        this.utilisateurRepository = utilisateurRepository;
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
        System.out.println("15. Gestion des options");
        System.out.println("16. Commander un entretien ponctuel");
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
                        OptionService.OPT_SIGNATURE_MANUELLE);
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
            case 15:
                gererMesOptions(agent);
                break;
            case 16:
                commanderEntretienPonctuel(agent);
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

    /**
     * Gérer les options de l'agent.
     */
    private void gererMesOptions(Agent agent) {
        while (true) {
            System.out.println("\n--- GESTION DES OPTIONS ---");
            System.out.println("1. Voir mes options actives");
            System.out.println("2. Souscrire à une nouvelle option");
            System.out.println("3. Résilier une option");
            System.out.println("4. Retour");
            System.out.print("Votre choix : ");

            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    afficherOptionsAgent(agent);
                    break;
                case 2:
                    souscrireOption(agent);
                    break;
                case 3:
                    resilierOption(agent);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Choix invalide !");
                    break;
            }
        }
    }

    /**
     * Afficher les options souscrites par l'agent.
     */
    private void afficherOptionsAgent(Agent agent) {
        List<OptionPayanteAgent> options = optionService.getOptionsByAgent(agent);
        if (options.isEmpty()) {
            System.out.println("\nVous n'avez aucune option souscrite.");
        } else {
            System.out.println("\n--- VOS ABONNEMENTS ---");
            for (OptionPayanteAgent opt : options) {
                String statut = opt.isEstActive() ? "[ACTIVE]" : "[RESILIEE]";
                System.out.println(
                        "- ID: " + opt.getId() + " | " + opt.getType() + " | " + opt.getPrixMensuel() + "€ " + statut);
            }
        }
    }

    /**
     * Souscrire à une nouvelle option payante.
     */
    private void souscrireOption(Agent agent) {
        System.out.println("\n--- SOUSCRIRE À UNE OPTION ---");
        System.out.println("Options disponibles :");
        System.out.println("1. Mise en avant premium (50€/mois)");
        System.out.println("2. Assurance étendue (30€/mois)");
        System.out.println("3. Support prioritaire (20€/mois)");
        System.out.print("Votre choix : ");

        int choixOption = sc.nextInt();
        sc.nextLine();

        String type;
        float prix;

        switch (choixOption) {
            case 1:
                type = "Mise en avant premium";
                prix = 50.0f;
                break;
            case 2:
                type = "Assurance étendue";
                prix = 30.0f;
                break;
            case 3:
                type = "Support prioritaire";
                prix = 20.0f;
                break;
            default:
                System.out.println("Choix invalide !");
                return;
        }

        optionService.souscrireNouvelleOption(agent, type, prix);
        System.out.println("\n✓ Option '" + type + "' souscrite avec succès pour " + prix + "€/mois.");
    }

    /**
     * Annuler une option payante existante.
     */
    private void resilierOption(Agent agent) {
        afficherOptionsAgent(agent);
        System.out.println("\n--- RÉSILIER UNE OPTION ---");
        System.out.print("Entrez l'ID de l'option à résilier : ");

        Long optionId = sc.nextLong();
        sc.nextLine();

        optionService.annulerOption(optionId);
        System.out.println("\n✓ Option résiliée (si elle existait).");
    }

    /**
     * Commander un entretien ponctuel pour un véhicule.
     */
    private void commanderEntretienPonctuel(Agent agent) {
        // Recharge l'agent depuis la base pour ouvrir une session propre
        Agent agentComplet = (Agent) utilisateurRepository.findById(agent.getId()).orElse(null);

        if (agentComplet == null) {
            System.out.println("Erreur: agent non trouvé.");
            return;
        }

        // 1. Sélection du véhicule de l'agent
        List<Vehicule> sesVehicules = agentComplet.getVehiculesEnLocation();
        if (sesVehicules.isEmpty()) {
            System.out.println("Vous n'avez pas de véhicules enregistrés.");
            return;
        }

        System.out.println("\nSélectionnez le véhicule à entretenir :");
        for (int i = 0; i < sesVehicules.size(); i++) {
            System.out
                    .println((i + 1) + ". " + sesVehicules.get(i).getMarque() + " " + sesVehicules.get(i).getModele());
        }
        int vIdx = sc.nextInt();
        sc.nextLine();

        if (vIdx < 1 || vIdx > sesVehicules.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        // 2. Sélection de l'entreprise (Prestataire)
        List<PrestataireEntretien> prestataires = prestataireRepository.findAll();
        if (prestataires.isEmpty()) {
            System.out.println("Aucun prestataire d'entretien disponible.");
            return;
        }

        System.out.println("\nChoisissez un prestataire d'entretien :");
        for (int i = 0; i < prestataires.size(); i++) {
            System.out.println((i + 1) + ". " + prestataires.get(i).getNomSociete() + " ("
                    + prestataires.get(i).getActivite() + ")");
        }
        int eIdx = sc.nextInt();
        sc.nextLine();

        if (eIdx < 1 || eIdx > prestataires.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        // 3. Appel au service
        optionService.commanderEntretien(agentComplet, sesVehicules.get(vIdx - 1), prestataires.get(eIdx - 1));
    }
}
