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

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              VehiculeRepository vehiculeRepository,
                              AssuranceService assuranceService,
                              ContratService contratService,
                              VehiculeService vehiculeService) {
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.vehiculeService = vehiculeService;
    }

    public Optional<Utilisateur> login(String email, String motDePasse) {
        return utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
    }

    public Utilisateur register(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur mettreAJour(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public void ajouterVehicule(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent agent)) {
            System.out.println("Erreur: Seul un Agent peut ajouter un véhicule.");
            return;
        }

        System.out.println("\n--- Ajout d'un vehicule ---");

        TypeVehicule type = saisirTypeVehicule();
        String inputMarque = saisirTexte("Marque: ");
        String inputModele = saisirTexte("Modele: ");
        String inputCouleur = saisirTexte("Couleur: ");
        EtatVehicule etat = saisirEtatVehicule();

        String rue = saisirTexte("Rue: ");
        String codePostal = saisirTexte("Code Postal: ");
        String ville = saisirTexte("Ville: ");

        Vehicule vehicule = new Vehicule(type, inputMarque, inputModele, inputCouleur, etat, rue, codePostal, ville);
        vehicule.ajouterDisponibilite(LocalDate.now().plusDays(1));
        vehicule.setAgent(agent);

        vehiculeRepository.save(vehicule);
        System.out.println("Véhicule ajouté !");
    }

    public void afficherLesVehiculesDeAgent(Agent agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent(agent);
        System.out.println("\n--- Véhicules de l'agent ---");
        if (vehicules.isEmpty()) {
            System.out.println("Aucun véhicule.");
            return;
        }
        for (Vehicule v : vehicules) {
            System.out.println(v);
        }
    }

    public void supprimerVehicule(Agent agent) {
        afficherLesVehiculesDeAgent(agent);
        System.out.print("ID du vehicule à supprimer: ");
        int id = lireIntSafe();

        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if (vehicule == null || vehicule.getAgent() == null || vehicule.getAgent().getId() != agent.getId()) {
            System.out.println("Erreur: Véhicule introuvable ou ne vous appartient pas.");
            return;
        }

        vehiculeRepository.delete(vehicule);
        System.out.println("Véhicule supprimé !");
    }

    public void modifierVehicule(Agent agent) {
        afficherLesVehiculesDeAgent(agent);
        System.out.print("ID du vehicule à modifier: ");
        int id = lireIntSafe();

        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if (vehicule == null || vehicule.getAgent() == null || vehicule.getAgent().getId() != agent.getId()) {
            System.out.println("Erreur: Véhicule introuvable ou ne vous appartient pas.");
            return;
        }

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

    public void louerVehicule(Utilisateur currentUser) {
        if (!(currentUser instanceof Loueur loueurCourant)) {
            System.out.println("Erreur: seul un loueur peut louer.");
            return;
        }

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
            if (dateDebut == null) return;

            System.out.print("Nombre de jours de location : ");
            int nbJours = lireIntSafe();
            if (nbJours <= 0) {
                System.out.println("Nombre de jours invalide.");
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

            System.out.println("\n=== Récapitulatif de Location ===");
            System.out.println("Véhicule: ID " + vehiculeId);
            System.out.println("Date de début: " + dateDebut);
            System.out.println("Durée: " + nbJours + " jours");
            System.out.println("Assurance: " + assuranceChoisie.getNom());
            System.out.println("Prix assurance: " + prixAssurance + "€");
            System.out.println("Prix total estimé: " + prixAssurance + "€");

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

            contratService.creerContratPresigne(
                    dateDebutContrat,
                    dateFinContrat,
                    agentVehicule,
                    loueurCourant,
                    vehiculeSelectionne,
                    prixAssurance
            );

            System.out.println("\n Location demandée !");
            System.out.println("Contrat pré-signé créé. L'agent doit maintenant l'accepter/refuser.");
            System.out.println("Période: du " + dateDebut + " au " + dateDebut.plusDays(nbJours));

        } catch (Exception e) {
            System.out.println("Erreur lors de la location: " + e.getMessage());
        }
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
                case "1": return TypeVehicule.Voiture;
                case "2": return TypeVehicule.Camion;
                case "3": return TypeVehicule.Moto;
                default: System.out.println("Type invalide.");
            }
        }
    }

    private EtatVehicule saisirEtatVehicule() {
        while (true) {
            System.out.print("Etat (Loue(1) / Non_loue(2)): ");
            String inputEtat = scanner.nextLine().trim();
            switch (inputEtat) {
                case "1": return EtatVehicule.Loué;
                case "2": return EtatVehicule.Non_loué;
                default: System.out.println("Etat invalide.");
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
}
