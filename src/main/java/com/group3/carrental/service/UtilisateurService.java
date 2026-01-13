package com.group3.carrental.service;

import java.util.Scanner;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Vehicule.EtatVehicule;
import com.group3.carrental.entity.Vehicule.TypeVehicule;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    private final Scanner scanner = new Scanner(System.in);

    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final AssuranceService assuranceService;
    private final ContratService contratService;
    private final VehiculeService vehiculeService;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, VehiculeRepository vehiculeRepository,
            AssuranceService assuranceService, ContratService contratService, VehiculeService vehiculeService) {
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

    /**
     * Met à jour les informations d'un utilisateur.
     */
    public Utilisateur mettreAJour(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    // Ajouter un véhicule pour agent
    public void ajouterVehicule(Utilisateur currentUser) {
        // Vérifier si l'utilisateur est un Agent
        if (!(currentUser instanceof Agent)) {
            System.out.println("Erreur: Seul un Agent peut ajouter un véhicule.");
            return;
        }
        Agent agent = (Agent) currentUser;

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
        Vehicule vehicule = new Vehicule(type, inputMarque, inputModele,
                inputCouleur, etat, rue, codePostal, ville);
        vehicule.ajouterDisponibilite(LocalDate.now().plusDays(1));
        vehicule.setAgent(agent); // Associer l'agent avant de sauvegarder
        vehiculeRepository.save(vehicule);
        System.out.println("Vehicule ajoute reussi !!");

    }

    // Afficher les vehicules de l'agent
    public void afficherLesVehiculesDeAgent(Agent agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent(agent);
        System.out.println("\n--- Liste des vehicules de l'agent " + " ---");
        for (Vehicule vehicule : vehicules) {
            System.out.println(vehicule);
        }
    }

    // Supprimer un véhicule pour agent
    public void supprimerVehicule(Agent agent) {
        afficherLesVehiculesDeAgent(agent);
        System.out.println("ID du vehicule a supprimer: ");
        int id = scanner.nextInt();
        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if (vehicule == null || vehicule.getAgent() == null || vehicule.getAgent().getId() != agent.getId()) {
            System.out.println("Erreur: Vehicule non trouve ou ne vous appartient pas.");
            return;
        }
        vehiculeRepository.delete(vehicule);
        System.out.println("Vehicule supprime reussi !!");
    }

    // Modifier un véhicule pour agent
    public void modifierVehicule(Agent agent) {
        afficherLesVehiculesDeAgent(agent);
        System.out.println("ID du vehicule a modifier: ");
        int id = scanner.nextInt();
        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if (vehicule == null || vehicule.getAgent() == null || vehicule.getAgent().getId() != agent.getId()) {
            System.out.println("Erreur: Vehicule non trouve ou ne vous appartient pas.");
            return;
        }
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
        System.out.println("RueLocalisation: ");
        String rueLocalisation = scanner.nextLine();
        System.out.println("CPostalLocalisation: ");
        String cPostalLocalisation = scanner.nextLine();
        System.out.println("VilleLocalisation: ");
        String villeLocalisation = scanner.nextLine();
        vehicule.setType(type);
        vehicule.setMarque(inputMarque);
        vehicule.setModele(inputModele);
        vehicule.setCouleur(inputCouleur);
        vehicule.setEtat(etat);
        vehicule.setRueLocalisation(rueLocalisation);
        vehicule.setCPostalLocalisation(cPostalLocalisation);
        vehicule.setVilleLocalisation(villeLocalisation);
        vehiculeRepository.save(vehicule);
        System.out.println("Vehicule modifie reussi !!");
    }

    public void louerVehicule(Utilisateur currentUser) {
        System.out.println("\n=== Location de Véhicule ===");

        try {
            vehiculeService.afficherVehiculesDisponibles();
            System.out.print("\nEntrez l'ID du véhicule à louer (disponible) : ");
            int vehiculeId = scanner.nextInt();
            scanner.nextLine();

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
                String dateInput = scanner.nextLine();
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
            int nbJours = scanner.nextInt();
            scanner.nextLine();

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
            int choixAssurance = scanner.nextInt();
            scanner.nextLine();

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
            String confirmation = scanner.nextLine();

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
            scanner.nextLine();
        }
    }

}
