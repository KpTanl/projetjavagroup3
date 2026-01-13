package com.group3.carrental.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Vehicule.EtatVehicule;
import com.group3.carrental.entity.Vehicule.TypeVehicule;
import com.group3.carrental.repository.VehiculeRepository;

/**
 * Service pour gérer les opérations sur les véhicules.
 */
@Service
public class VehiculeService {

    Scanner scanner = new Scanner(System.in);
    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public VehiculeService(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    public void afficherTousLesVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();

        System.out.println("\n--- Liste des vehicules ---");
        if (vehicules.isEmpty()) {
            System.out.println("Aucun vehicule disponible.");
        } else {
            for (Vehicule v : vehicules) {
                System.out.println("------------------------------------");
                System.out.println("ID: " + v.getId());
                System.out.println("Type: " + v.getType());
                System.out.println("Vehicule: " + v.getMarque() + " " + v.getModele());
                System.out.println("Lieu: " + v.getLocalisationComplete());
                System.out.println("Etat: " + v.getEtat());
                Double note = v.calculerNoteMoyenne();
                System.out.println("Note moyenne: " + (note != null ? note + "/5" : "Aucune note"));
                // Afficher l'agent associé
                if (v.getAgent() != null) {
                    System.out.println("Agent: " + v.getAgent().getPrenom() + " " + v.getAgent().getNom() +
                            " (" + v.getAgent().getEmail() + ")");
                } else {
                    System.out.println("Agent: Non assigné");
                }
                System.out.print("Dates disponibles: ");
                if (v.getDatesDisponibles().isEmpty()) {
                    System.out.println("Aucune");
                } else {
                    System.out.println(v.getDatesDisponibles());
                }
                System.out.println("------------------------------------");
            }
        }
        System.out.println("Total vehicules en base: " + vehicules.size());
    }

    /**
     * Affiche uniquement les véhicules disponibles (etat = Non_loué).
     */
    public void afficherVehiculesDisponibles() {
        List<Vehicule> disponibles = vehiculeRepository.findByEtat(Vehicule.EtatVehicule.Non_loué);

        disponibles = disponibles.stream()
                .filter(v -> !v.getDatesDisponibles().isEmpty())
                .toList();

        System.out.println("\n--- Véhicules disponibles (non loués) ---");
        if (disponibles.isEmpty()) {
            System.out.println("Aucun véhicule disponible pour le moment.");
        } else {
            for (Vehicule v : disponibles) {
                System.out.println("------------------------------------");
                System.out.println("ID: " + v.getId());
                System.out.println("Type: " + v.getType());
                System.out.println("Vehicule: " + v.getMarque() + " " + v.getModele());
                System.out.println("Lieu: " + v.getLocalisationComplete());
                System.out.println("Etat: " + v.getEtat());
                Double noteDisp = v.calculerNoteMoyenne();
                System.out.println("Note moyenne: " + (noteDisp != null ? noteDisp + "/5" : "Aucune note"));
                // Afficher l'agent associé
                if (v.getAgent() != null) {
                    System.out.println("Agent: " + v.getAgent().getPrenom() + " " + v.getAgent().getNom() +
                            " (" + v.getAgent().getEmail() + ")");
                } else {
                    System.out.println("Agent: Non assigné");
                }
                System.out.println("Dates disponibles: " + v.getDatesDisponibles());
                System.out.println("------------------------------------");
            }
        }
        System.out.println("Total véhicules disponibles: " + disponibles.size());
    }

    /**
     * Retourne tous les véhicules.
     */
    public List<Vehicule> getTousLesVehicules() {
        return vehiculeRepository.findAll();
    }

    /**
     * Récupère un véhicule par son ID.
     */
    public Vehicule getVehiculeById(int id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    /**
     * Récupère un véhicule disponible (etat = Non_loué) par son ID.
     * Retourne null si le véhicule n'existe pas ou n'est pas disponible.
     */
    public Vehicule getVehiculeDisponibleById(int id) {
        Vehicule v = getVehiculeById(id);
        if (v == null)
            return null;
        return v.getEtat() == Vehicule.EtatVehicule.Non_loué ? v : null;
    }

    // Ajouter un véhicule pour agent
    public void ajouterVehicule() {
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
        vehiculeRepository.save(vehicule);
        System.out.println("Vehicule ajoute reussi !!");
    }

    public void supprimerVehicule(int id) {
        vehiculeRepository.deleteById(id);
    }

    public void filtrerVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        if (vehicules.isEmpty()) {
            System.out.println("Aucun vehicule disponible.");
            return;
        }

        String villesPossibles = vehicules.stream()
                .map(Vehicule::getVilleLocalisation)
                .distinct()
                .collect(java.util.stream.Collectors.joining(" / "));
        System.out.println("Dans quelle ville cherchez-vous ? (Disponibles : " + villesPossibles + ")");
        String villeSaisie = scanner.nextLine();

        String marquesPossibles = vehicules.stream()
                .map(Vehicule::getMarque)
                .distinct()
                .collect(java.util.stream.Collectors.joining(" / "));
        System.out.println("Quelle marque ? (Disponibles : " + marquesPossibles + ")");
        String marqueSaisie = scanner.nextLine();

        String couleursPossibles = vehicules.stream()
                .map(Vehicule::getCouleur)
                .distinct()
                .collect(java.util.stream.Collectors.joining(" / "));
        System.out.println("Quelle couleur ? (Disponibles : " + couleursPossibles + ")");
        String couleurSaisie = scanner.nextLine();

        System.out.println("Note minimale souhaitee (ex: 4.0 ou Entree pour 0.0 = pas de filtre) :");
        String noteInput = scanner.nextLine();
        double noteSaisie = noteInput.isEmpty() ? 0.0 : Double.parseDouble(noteInput);

        LocalDate demain = LocalDate.now().plusDays(1);
        List<Vehicule> resultats = vehicules.stream()
                .filter(v -> v.getVilleLocalisation().equalsIgnoreCase(villeSaisie))
                .filter(v -> v.getDatesDisponibles().contains(demain))
                .filter(v -> v.getMarque().equalsIgnoreCase(marqueSaisie))
                .filter(v -> {
                    Double noteVehicule = v.calculerNoteMoyenne();
                    if (noteSaisie == 0.0) return true;
                    return noteVehicule != null && noteVehicule >= noteSaisie;
                })
                .filter(v -> v.getCouleur().equalsIgnoreCase(couleurSaisie))
                .collect(java.util.stream.Collectors.toList());

        System.out.println("\n--- RESULTATS CORRESPONDANTS ---");
        if (resultats.isEmpty()) {
            System.out.println("Aucun vehicule ne correspond a vos criteres.");
        } else {
            for (Vehicule v : resultats) {
                System.out.println("------------------------------------");
                System.out.println("Vehicule : " + v.getMarque() + " " + v.getModele() + " (" + v.getCouleur() + ")");
                System.out.println("Lieu : " + v.getLocalisationComplete());
                Double noteResult = v.calculerNoteMoyenne();
                System.out.println("Note : " + (noteResult != null ? noteResult + "/5" : "Aucune note"));
                System.out.println("------------------------------------");
            }
        }
    }
}
