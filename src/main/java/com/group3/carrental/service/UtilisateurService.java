package com.group3.carrental.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Parking;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Vehicule.EtatVehicule;
import com.group3.carrental.entity.Vehicule.TypeVehicule;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;

@Service
public class UtilisateurService {
    private final Scanner scanner = new Scanner(System.in);

    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final VehiculeService vehiculeService;
    private final ParkingService parkingService;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository,
            VehiculeRepository vehiculeRepository,
            AssuranceService assuranceService,
            ContratService contratService,
            VehiculeService vehiculeService,
            ParkingService parkingService) {
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.vehiculeService = vehiculeService;
        this.parkingService = parkingService;
    }

    public Optional<Utilisateur> login(String email, String motDePasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent() && utilisateurOpt.get().seConnecter(motDePasse)) {
            return utilisateurOpt;
        }

        return Optional.empty();
    }

    public Utilisateur register(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur mettreAJour(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElse(null);
    }

    public void ajouterVehicule(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent agent)) {
            System.out.println("Erreur: Seul un Agent peut ajouter un véhicule.");
            return;
        }

        System.out.println("\n--- Ajout d'un vehicule ---");
        System.out.println("Type (Voiture(1) / Camion(2) / Moto(3)): ");
        String inputType = scanner.nextLine();
        TypeVehicule type = null;
        while (type == null) {
            switch (inputType) {
                case "1":
                    type = TypeVehicule.Voiture;
                    break;
                case "2":
                    type = TypeVehicule.Camion;
                    break;
                case "3":
                    type = TypeVehicule.Moto;
                    break;
                default:
                    System.out.println("Type invalide. Veuillez choisir un type valide.");
                    break;
            }
        }
        System.out.println("Marque: ");
        String inputMarque = scanner.nextLine();
        System.out.println("Modele: ");
        String inputModele = scanner.nextLine();
        System.out.println("Couleur: ");
        String inputCouleur = scanner.nextLine();
        System.out.println("Etat (Loue(1) / Non_loue(2)): ");
        String inputEtat = scanner.nextLine();
        EtatVehicule etat = null;
        while (etat == null) {
            switch (inputEtat) {
                case "1":
                    etat = EtatVehicule.Loué;
                    break;
                case "2":
                    etat = EtatVehicule.Non_loué;
                    break;
                default:
                    System.out.println("Type invalide. Veuillez choisir un type valide.");
                    break;
            }
        }
        System.out.println("Rue: ");
        String rue = scanner.nextLine();
        System.out.println("Code Postal: ");
        String codePostal = scanner.nextLine();
        System.out.println("Ville: ");
        String ville = scanner.nextLine();
        System.out.println("Latitude: ");
        double latitude = Double.parseDouble(scanner.nextLine());
        System.out.println("Longitude: ");
        double longitude = Double.parseDouble(scanner.nextLine());

        // Demander le prix journalier du véhicule
        System.out.println("Prix journalier (€/jour): ");
        double prixJournalier = Double.parseDouble(scanner.nextLine());

        Vehicule vehicule = new Vehicule(type, inputMarque, inputModele,
                inputCouleur, etat, rue, codePostal, ville, latitude, longitude);
        vehicule.setPrixJournalier(prixJournalier);

        // Demander les dates de disponibilité (optionnel - vide = toujours disponible)
        System.out.println("\n--- Dates de disponibilité ---");
        System.out.println("(Laissez vide pour 'toujours disponible')");
        System.out.print("Date de début disponible (YYYY-MM-DD) : ");
        String dateDebutStr = scanner.nextLine().trim();

        if (!dateDebutStr.isEmpty()) {
            LocalDate dateDebut = null;
            LocalDate dateFin = null;

            // Valider date de début
            while (dateDebut == null) {
                try {
                    dateDebut = LocalDate.parse(dateDebutStr);
                } catch (Exception e) {
                    System.out.println("Format invalide. Utilisez le format YYYY-MM-DD (ex: 2026-01-20)");
                    System.out.print("Date de début disponible (YYYY-MM-DD) : ");
                    dateDebutStr = scanner.nextLine().trim();
                }
            }

            // Valider date de fin
            while (dateFin == null || dateFin.isBefore(dateDebut)) {
                System.out.print("Date de fin disponible (YYYY-MM-DD) : ");
                String dateFinStr = scanner.nextLine().trim();
                try {
                    dateFin = LocalDate.parse(dateFinStr);
                    if (dateFin.isBefore(dateDebut)) {
                        System.out.println("Erreur: La date de fin doit être après ou égale à " + dateDebut);
                        dateFin = null;
                    }
                } catch (Exception e) {
                    System.out.println("Format invalide. Utilisez le format YYYY-MM-DD (ex: 2026-01-25)");
                }
            }

            // Ajouter toutes les dates de la plage
            LocalDate current = dateDebut;
            while (!current.isAfter(dateFin)) {
                vehicule.ajouterDisponibilite(current);
                current = current.plusDays(1);
            }
            System.out.println("Disponibilité ajoutée du " + dateDebut + " au " + dateFin);
        } else {
            // Par défaut, le véhicule est disponible pendant 60 jours à partir de la date
            // d'ajout
            LocalDate today = LocalDate.now();
            LocalDate sixtyDaysLater = today.plusDays(60);
            LocalDate current = today;
            while (!current.isAfter(sixtyDaysLater)) {
                vehicule.ajouterDisponibilite(current);
                current = current.plusDays(1);
            }
            System.out.println("Le véhicule sera disponible du " + today + " au " + sixtyDaysLater + " (60 jours).");
        }

        vehicule.setAgent(agent);
        vehiculeRepository.save(vehicule);
        System.out.println("Vehicule ajoute reussi !!");
    }

    public void afficherLesVehiculesDeAgent(Utilisateur agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent((Agent) agent);
        System.out.println("\n--- Véhicules de l'agent ---");
        if (vehicules.isEmpty()) {
            System.out.println("Aucun véhicule trouvé.");
        } else {
            for (int i = 0; i < vehicules.size(); i++) {
                Vehicule v = vehicules.get(i);
                System.out.println((i + 1) + ". " + v.getMarque() + " " + v.getModele() +
                        " (" + v.getCouleur() + ") - État: " + v.getEtat());
            }
        }
    }

    public void supprimerVehicule(Agent agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent(agent);
        if (vehicules.isEmpty()) {
            System.out.println("Aucun véhicule à supprimer.");
            return;
        }

        afficherLesVehiculesDeAgent(agent);
        System.out.print("Numéro du vehicule à supprimer: ");
        int numero = lireIntSafe();

        if (numero < 1 || numero > vehicules.size()) {
            System.out.println("Erreur: Numéro invalide.");
            return;
        }

        Vehicule vehicule = vehicules.get(numero - 1);

        // Vérifier s'il y a des contrats actifs ou futurs
        List<com.group3.carrental.entity.Contrat> contrats = contratService.getContratsParVehicule(vehicule.getId());
        LocalDate aujourdhui = LocalDate.now();

        // Filtrer les contrats en cours ou futurs
        List<com.group3.carrental.entity.Contrat> contratsActifsOuFuturs = contrats.stream()
                .filter(c -> {
                    LocalDate dateFin = c.getDateFin().toInstant()
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    return !dateFin.isBefore(aujourdhui);
                })
                .toList();

        if (contratsActifsOuFuturs.isEmpty()) {
            // Pas de contrats actifs - suppression possible
            try {
                // 1. Supprimer TOUS les contrats associés (terminés inclus)
                List<com.group3.carrental.entity.Contrat> tousLesContrats = contratService
                        .getContratsParVehicule(vehicule.getId());
                for (com.group3.carrental.entity.Contrat contrat : tousLesContrats) {
                    contratService.supprimerContrat(contrat.getId());
                }

                // 2. Nettoyer les notes et relations
                vehicule.getNotesRecues().clear();
                vehicule.setAgent(null);
                vehicule.setParkingPartenaire(null);
                vehiculeRepository.save(vehicule);

                // 3. Supprimer le véhicule
                vehiculeRepository.delete(vehicule);
                vehiculeRepository.flush();

                System.out.println("Véhicule supprimé avec succès !");
            } catch (Exception e) {
                System.out.println("Erreur lors de la suppression : " + e.getMessage());
                System.out.println("Détails: Le véhicule a peut-être des dépendances non gérées.");
            }
        } else {
            System.out.println("Erreur: Véhicule a des contrats actifs ou futurs.");
        }
    }

    public void modifierVehicule(Agent agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent(agent);
        if (vehicules.isEmpty()) {
            System.out.println("Aucun véhicule à modifier.");
            return;
        }

        afficherLesVehiculesDeAgent(agent);
        System.out.print("Numéro du vehicule à modifier: ");
        int numero = lireIntSafe();

        if (numero < 1 || numero > vehicules.size()) {
            System.out.println("Erreur: Numéro invalide.");
            return;
        }

        Vehicule vehicule = vehicules.get(numero - 1);

        System.out.println("\n--- Modification du véhicule ---");
        TypeVehicule type = saisirTypeVehicule();
        String inputMarque = saisirTexte("Marque: ");
        String inputModele = saisirTexte("Modele: ");
        String inputCouleur = saisirTexte("Couleur: ");
        EtatVehicule etat = saisirEtatVehicule();

        String rueLocalisation = saisirTexte("RueLocalisation: ");
        String cPostalLocalisation = saisirTexte("CPostalLocalisation: ");
        String villeLocalisation = saisirTexte("VilleLocalisation: ");

        vehicule.setType(type);
        vehicule.setMarque(inputMarque);
        vehicule.setModele(inputModele);
        vehicule.setCouleur(inputCouleur);
        vehicule.setEtat(etat);
        vehicule.setRueLocalisation(rueLocalisation);
        vehicule.setCPostalLocalisation(cPostalLocalisation);
        vehicule.setVilleLocalisation(villeLocalisation);

        vehiculeRepository.save(vehicule);
        System.out.println("Véhicule modifié !");
    }

    public List<Utilisateur> findAllAgents() {
        return utilisateurRepository.findByRole(Utilisateur.Role.Agent);
    }

    public void louerVehicule(Utilisateur currentUser) {
        // Autoriser un agent à louer comme un loueur
        if (!(currentUser instanceof Loueur) && !(currentUser instanceof Agent)) {
            System.out.println("Erreur: seul un loueur ou un agent peut louer.");
            return;
        }

        Utilisateur locataire = currentUser;

        System.out.println("\n=== Location de Véhicule ===");

        try {
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = lireIntSafe();

            Vehicule vehiculeSelectionne = vehiculeService.getVehiculeDisponibleById(vehiculeId);
            if (vehiculeSelectionne == null) {
                System.out.println("Le véhicule choisi n'est pas disponible ou n'existe pas.");
                return;
            }

            if (vehiculeSelectionne.getAgent() == null) {
                System.out.println("Ce véhicule n'a pas d'agent associé, impossible de créer un contrat.");
                return;
            }

            System.out.println("Véhicule sélectionné: " + vehiculeSelectionne.getMarque() + " "
                    + vehiculeSelectionne.getModele() + " (ID: " + vehiculeId + ")");

            List<LocalDate> datesDisponibles = vehiculeSelectionne.getDatesDisponibles();
            if (datesDisponibles == null || datesDisponibles.isEmpty()) {
                System.out.println("Aucune date disponible pour ce véhicule.");
                return;
            }

            LocalDate dateDebut = choisirDateDebut(datesDisponibles);
            if (dateDebut == null)
                return;

            System.out.print("Nombre de jours de location : ");
            int nbJours = lireIntSafe();
            if (nbJours <= 0) {
                System.out.println("Nombre de jours invalide.");
                return;
            }

            // Vérifier que TOUTES les dates de la période de location sont disponibles
            LocalDate dateFin = dateDebut.plusDays(nbJours - 1);
            List<LocalDate> datesManquantes = new java.util.ArrayList<>();
            LocalDate dateCheck = dateDebut;
            while (!dateCheck.isAfter(dateFin)) {
                if (!datesDisponibles.contains(dateCheck)) {
                    datesManquantes.add(dateCheck);
                }
                dateCheck = dateCheck.plusDays(1);
            }

            if (!datesManquantes.isEmpty()) {
                System.out.println("Erreur : Le véhicule n'est pas disponible pour toute la période demandée.");
                System.out.println("Dates non disponibles : " + datesManquantes);
                return;
            }

            List<Assurance> assurances = assuranceService.getAllAssurances();
            if (assurances.isEmpty()) {
                System.out.println("Aucune assurance disponible.");
                return;
            }

            System.out.println("\n=== Assurances Disponibles ===");
            for (int i = 0; i < assurances.size(); i++) {
                Assurance a = assurances.get(i);
                double prix = assuranceService.calculerPrix(a, nbJours);
                System.out.println((i + 1) + ". " + a.getNom() +
                        " - " + a.getPrixParJour() + "€/jour" +
                        " (Total: " + prix + "€ pour " + nbJours + " jours)");
            }

            System.out.print("\nChoisissez une assurance (numéro) : ");
            int choixAssurance = lireIntSafe();
            if (choixAssurance < 1 || choixAssurance > assurances.size()) {
                System.out.println("Choix invalide !");
                return;
            }

            Assurance assuranceChoisie = assurances.get(choixAssurance - 1);
            double prixAssurance = assuranceService.calculerPrix(assuranceChoisie, nbJours);

            // OPTION PARKING
            Parking parkingSelectionne = null;
            System.out.print("\nVoulez-vous choisir un parking pour le dépôt ? (O/N) : ");
            if (scanner.nextLine().equalsIgnoreCase("O")) {
                parkingSelectionne = gererSelectionParkingPourLoueur();
            }

            double prixParkingOuReduction = (parkingSelectionne != null) ? parkingSelectionne.getReductionloueur() : 0;

            // Calcul du prix du véhicule (prix journalier défini par l'agent)
            double prixVehicule = vehiculeSelectionne.getPrixJournalier() * nbJours;

            // Calcul des frais de plateforme (10% + 2€ par jour)
            double fraisPlateforme = (prixVehicule * 0.10) + (2.0 * nbJours);

            // Prix total = prix véhicule + frais plateforme + assurance - réduction parking
            double prixTotal = prixVehicule + fraisPlateforme + prixAssurance - prixParkingOuReduction;

            // AFFICHAGE DES PRIX
            System.out.println("\n--- Détails du paiement ---");
            System.out.println("Prix véhicule : " + prixVehicule + " euros (" + vehiculeSelectionne.getPrixJournalier()
                    + "€/jour x " + nbJours + " jours)");
            System.out.println(
                    "Frais de plateforme : " + String.format("%.2f", fraisPlateforme) + " euros (10% + 2€/jour)");
            System.out.println("Prix assurance : " + prixAssurance + " euros");
            if (parkingSelectionne != null) {
                System.out.println("Réduction parking : -" + prixParkingOuReduction + " euros");
            }
            System.out.println("PRIX TOTAL ESTIMÉ : " + String.format("%.2f", prixTotal) + " euros");

            // RÉCAPITULATIF
            System.out.println("\n=== Récapitulatif de Location ===");
            System.out.println("Véhicule: ID " + vehiculeId + " - " + vehiculeSelectionne.getMarque() + " "
                    + vehiculeSelectionne.getModele());
            System.out.println("Prix journalier: " + vehiculeSelectionne.getPrixJournalier() + "€/jour");
            System.out.println("Date de début: " + dateDebut);
            System.out.println("Durée: " + nbJours + " jours");
            System.out.println("Assurance: " + assuranceChoisie.getNom());
            System.out.println("Prix assurance: " + prixAssurance + "€");
            if (parkingSelectionne != null) {
                System.out.println("Lieu de dépôt : " + parkingSelectionne.getNomP() + " ("
                        + parkingSelectionne.getVilleP() + ")");
            }
            System.out.println("Prix total estimé: " + String.format("%.2f", prixTotal) + "€");

            System.out.print("\nConfirmer la location ? (O/N) : ");
            String confirmation = scanner.nextLine();

            if (!confirmation.equalsIgnoreCase("O")) {
                System.out.println("Location annulée.");
                return;
            }

            java.util.Date dateDebutContrat = java.util.Date.from(
                    dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.util.Date dateFinContrat = java.util.Date.from(
                    dateDebut.plusDays(nbJours).atStartOfDay(ZoneId.systemDefault()).toInstant());

            Agent agentVehicule = vehiculeSelectionne.getAgent();

            // Gestion du crédit du portefeuille (pour loueur ou agent)
            double creditUtilise = 0;
            if (locataire != null && locataire.getSoldePorteMonnaie() > 0) {
                System.out.println("\n*** CRÉDIT DISPONIBLE ***");
                System.out.println("Vous avez " + locataire.getSoldePorteMonnaie() + "€ de crédit.");
                System.out.print("Voulez-vous utiliser votre crédit ? (O/N) : ");
                String utiliserCredit = scanner.nextLine();
                if (utiliserCredit.equalsIgnoreCase("O")) {
                    creditUtilise = Math.min(prixTotal, locataire.getSoldePorteMonnaie());
                    prixTotal -= creditUtilise;
                    locataire.setSoldePorteMonnaie(locataire.getSoldePorteMonnaie() - creditUtilise);
                    mettreAJour(locataire);
                    System.out.println("Crédit utilisé : -" + creditUtilise + "€");
                    System.out.println("Nouveau prix total : " + prixTotal + "€");
                }
            }

            contratService.creerContratPresigne(
                    dateDebutContrat,
                    dateFinContrat,
                    agentVehicule,
                    locataire,
                    vehiculeSelectionne,
                    prixTotal);

            System.out.println("\n Location demandée !");
            System.out.println("Contrat pré-signé créé. L'agent doit maintenant l'accepter/refuser.");
            System.out.println("Période: du " + dateDebut + " au " + dateDebut.plusDays(nbJours));

        } catch (Exception e) {
            System.out.println("Erreur lors de la location: " + e.getMessage());
        }
    }

    /**
     * Permet au loueur de sélectionner un parking partenaire pour le dépôt.
     */
    public Parking gererSelectionParkingPourLoueur() {
        List<Parking> tousLesParkings = parkingService.getAllParkings();
        List<String> villesDispo = Parking.getVillesDisponibles(tousLesParkings);

        if (villesDispo.isEmpty()) {
            System.out.println("Aucun parking avec des places disponibles actuellement.");
            return null;
        }

        System.out.println("\n--- Villes avec parkings partenaires ---");
        villesDispo.forEach(v -> System.out.println("- " + v));

        System.out.print("\nDans quelle ville souhaitez-vous déposer le véhicule ? ");
        String villeSaisie = scanner.nextLine().trim();

        List<Parking> parkingsDeLaVille = tousLesParkings.stream()
                .filter(p -> p.getVilleP() != null && p.getVilleP().equalsIgnoreCase(villeSaisie))
                .toList();

        if (parkingsDeLaVille.isEmpty()) {
            System.out.println("Désolé, pas de parking partenaire dans cette ville.");
            return null;
        }

        for (int i = 0; i < parkingsDeLaVille.size(); i++) {
            System.out.println((i + 1) + " - " + parkingsDeLaVille.get(i).getNomP());
        }

        System.out.print("\nChoisissez un numéro : ");
        int index = lireIntSafe();

        if (index > 0 && index <= parkingsDeLaVille.size()) {
            Parking choisi = parkingsDeLaVille.get(index - 1);
            choisi.afficherDetails();
            return choisi;
        }
        return null;
    }

    private int lireIntSafe() {
        while (true) {
            try {
                int x = Integer.parseInt(scanner.nextLine().trim());
                return x;
            } catch (Exception e) {
                System.out.print("Veuillez saisir un nombre valide : ");
            }
        }
    }

    private String saisirTexte(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private TypeVehicule saisirTypeVehicule() {
        while (true) {
            System.out.print("Type (Voiture(1) / Camion(2) / Moto(3)): ");
            String inputType = scanner.nextLine().trim();
            switch (inputType) {
                case "1":
                    return TypeVehicule.Voiture;
                case "2":
                    return TypeVehicule.Camion;
                case "3":
                    return TypeVehicule.Moto;
                default:
                    System.out.println("Type invalide.");
            }
        }
    }

    private EtatVehicule saisirEtatVehicule() {
        while (true) {
            System.out.print("Etat (Loue(1) / Non_loue(2)): ");
            String inputEtat = scanner.nextLine().trim();
            switch (inputEtat) {
                case "1":
                    return EtatVehicule.Loué;
                case "2":
                    return EtatVehicule.Non_loué;
                default:
                    System.out.println("Etat invalide.");
            }
        }
    }

    private LocalDate choisirDateDebut(List<LocalDate> datesDisponibles) {
        System.out.println("\nDates disponibles : ");
        System.out.println("Première: " + datesDisponibles.get(0));
        System.out.println("Dernière: " + datesDisponibles.get(datesDisponibles.size() - 1));

        if (datesDisponibles.size() == 1) {
            System.out.println("Date de début imposée: " + datesDisponibles.get(0));
            return datesDisponibles.get(0);
        }

        System.out.print("Saisissez la date de début (AAAA-MM-JJ) : ");
        String dateInput = scanner.nextLine();
        try {
            LocalDate dateDebut = LocalDate.parse(dateInput);
            if (!datesDisponibles.contains(dateDebut)) {
                System.out.println("Cette date n'est pas disponible.");
                return null;
            }
            return dateDebut;
        } catch (Exception e) {
            System.out.println("Format invalide.");
            return null;
        }
    }

    /**
     * Affiche la liste des agents et permet de consulter leurs véhicules.
     * Méthode partagée entre VisitorController et AgentController.
     */
    public void consulterAgents() {
        System.out.println("\n--- CONSULTATION DES AGENTS ---");
        List<Utilisateur> agents = findAllAgents();

        if (agents.isEmpty()) {
            System.out.println("Désolé, aucun agent n'est inscrit pour le moment.");
            return;
        }

        for (int i = 0; i < agents.size(); i++) {
            Utilisateur a = agents.get(i);
            System.out.println((i + 1) + ". " + a.getPrenom() + " " + a.getNom() + " (Email: " + a.getEmail() + ")");
        }

        System.out.print("\nEntrez le numéro de l'agent pour voir ses véhicules (ou 0 pour annuler) : ");
        if (scanner.hasNextInt()) {
            int choix = scanner.nextInt();
            scanner.nextLine();

            if (choix > 0 && choix <= agents.size()) {
                Utilisateur agentChoisi = agents.get(choix - 1);

                System.out.println("\n-----------------------------------------");
                System.out.println("VÉHICULES PROPOSÉS PAR " + agentChoisi.getPrenom().toUpperCase());
                System.out.println("-----------------------------------------");
                afficherLesVehiculesDeAgent(agentChoisi);
                System.out.println("-----------------------------------------");
            }
        } else {
            scanner.next();
            System.out.println("Saisie invalide.");
        }
    }
}
