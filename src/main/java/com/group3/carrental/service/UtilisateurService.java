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
import com.group3.carrental.entity.Parking;
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
     private final ParkingService parkingService;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, VehiculeRepository vehiculeRepository,
            AssuranceService assuranceService, ContratService contratService, VehiculeService vehiculeService, ParkingService parkingService) {
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.assuranceService = assuranceService;
        this.contratService = contratService;
        this.vehiculeService = vehiculeService;
        this.parkingService = parkingService;
       
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
            System.out.print("\nEntrez l'ID du véhicule à louer : ");
            int vehiculeId = scanner.nextInt();
            scanner.nextLine();

            Vehicule vehiculeSelectionne = vehiculeService.getVehiculeDisponibleById(vehiculeId);
            if (vehiculeSelectionne == null) {
                System.out.println("Véhicule non disponible.");
                return;
            }

            System.out.print("Nombre de jours de location : ");
            int nbJours = scanner.nextInt();
            scanner.nextLine();

           List<Assurance> assurances = assuranceService.getAllAssurances();
        if (assurances.isEmpty()) {
            System.out.println("Erreur : Aucune assurance disponible dans le système.");
            return;
        }

        System.out.println("\nAssurances disponibles :");
        for (int i = 0; i < assurances.size(); i++) {
            System.out.println((i + 1) + ". " + assurances.get(i).getNom() + " (" + assurances.get(i).getPrixParJour() + "€/jour)");
        }

        System.out.print("\nChoisissez une assurance (numéro) : ");
        int choixAssurance = scanner.nextInt();
        scanner.nextLine();

        if (choixAssurance < 1 || choixAssurance > assurances.size()) {
            System.out.println("Choix d'assurance invalide.");
            return;
        }

            Assurance assuranceChoisie = assurances.get(choixAssurance - 1);
            double prixAssurance = assuranceService.calculerPrix(assuranceChoisie, nbJours);

            // OPTION PARKING
            Parking parkingSelectionne = null;
            System.out.print("\nVoulez-vous choisir un parking pour le dépôt ? (O/N) : ");
            if (scanner.nextLine().equalsIgnoreCase("O")) {
                // APPEL de la méthode de sélection
                parkingSelectionne = gererSelectionParkingPourLoueur(); 
            }

            double prixParkingOuReduction = (parkingSelectionne != null) ? parkingSelectionne.getReductionloueur() : 0;
            double prixTotal = prixAssurance - prixParkingOuReduction; 

            // AFFICHAGE DES PRIX
            System.out.println("\n--- Détails du paiement ---");
            System.out.println("Prix assurance : " + prixAssurance + "euros");
            if (parkingSelectionne != null) {
                System.out.println("Réduction parking (Loueur) : -" + prixParkingOuReduction + "euros");
            }
            System.out.println("PRIX TOTAL ESTIMÉ : " + prixTotal + "euros");

            // RÉCAPITULATIF
            System.out.println("\n=== Récapitulatif de Location ===");
            System.out.println("Véhicule : " + vehiculeSelectionne.getMarque() + " " + vehiculeSelectionne.getModele());
            System.out.println("Assurance : " + assuranceChoisie.getNom());
            
            if (parkingSelectionne != null) {
                System.out.println("Lieu de dépôt : " + parkingSelectionne.getNomP() + " (" + parkingSelectionne.getVilleP() + ")");
            }
            System.out.print("\nConfirmer la location ? (O/N) : ");
            if (scanner.nextLine().equalsIgnoreCase("O")) {
                // Création du contrat...
                // (Note: assurez-vous que vos dates sont gérées ici comme dans votre code initial)
                System.out.println("Location confirmée !");
            }

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

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
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index > 0 && index <= parkingsDeLaVille.size()) {
            Parking choisi = parkingsDeLaVille.get(index - 1);
            choisi.afficherDetails();
            return choisi;
        }
        return null;
    }}
