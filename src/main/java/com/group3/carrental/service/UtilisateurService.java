package com.group3.carrental.service;

import java.util.Scanner;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Vehicule.EtatVehicule;
import com.group3.carrental.entity.Vehicule.TypeVehicule;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    private final Scanner scanner = new Scanner(System.in);

    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, VehiculeRepository vehiculeRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    public Optional<Utilisateur> login(String email, String motDePasse) {
        return utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
    }

    public Utilisateur register(Utilisateur utilisateur) {
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
    public void afficherLesVehiculesDeAgent(Utilisateur agent) {
        List<Vehicule> vehicules = vehiculeRepository.findByAgent(agent);
        System.out.println("\n--- Liste des vehicules de l'agent " + " ---");
        for (Vehicule vehicule : vehicules) {
            System.out.println(vehicule);
        }
    }

    // Supprimer un véhicule pour agent
    public void supprimerVehicule(Utilisateur agent) {
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
    public void modifierVehicule(Utilisateur agent) {
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

}
