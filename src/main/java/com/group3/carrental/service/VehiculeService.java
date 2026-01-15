package com.group3.carrental.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Vehicule.EtatVehicule;
import com.group3.carrental.entity.Vehicule.TypeVehicule;
import com.group3.carrental.repository.VehiculeRepository;

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

    public void afficherVehiculesDisponibles() {
        // Liste vide = disponible à tout moment
        // Exclure les véhicules avec suppression programmée
        List<Vehicule> disponibles = vehiculeRepository.findByEtat(Vehicule.EtatVehicule.Non_loué)
                .stream()
                .filter(v -> v.getDateSuppressionPrevue() == null)
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
                if (v.getAgent() != null) {
                    System.out.println("Agent: " + v.getAgent().getPrenom() + " " + v.getAgent().getNom() +
                            " (" + v.getAgent().getEmail() + ")");
                } else {
                    System.out.println("Agent: Non assigné");
                }
                // Afficher les dates ou "Toujours disponible"
                if (v.getDatesDisponibles().isEmpty()) {
                    System.out.println("Disponibilité: Toujours disponible");
                } else {
                    System.out.println("Dates disponibles: " + v.getDatesDisponibles());
                }
                System.out.println("------------------------------------");
            }
        }
        System.out.println("Total véhicules disponibles: " + disponibles.size());
    }

    public List<Vehicule> getTousLesVehicules() {
        return vehiculeRepository.findAll();
    }

    public List<Vehicule> getVehiculesByAgentId(int agentId) {
        return vehiculeRepository.findByAgentId(agentId);
    }

    public Vehicule getVehiculeById(int id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    public Vehicule getVehiculeDisponibleById(int id) {
        Vehicule v = getVehiculeById(id);
        if (v == null)
            return null;
        return v.getEtat() == Vehicule.EtatVehicule.Non_loué ? v : null;
    }

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
                case "3": 
                etat = EtatVehicule.Indisponible;
                System.out.println("Le véhicule est marqué comme : indisponible à la location");
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
        Vehicule vehicule = new Vehicule(type, inputMarque, inputModele,
                inputCouleur, etat, rue, codePostal, ville, latitude, longitude);
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
                    if (noteSaisie == 0.0)
                        return true;
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

    /**
     * Sauvegarde les modifications d'un véhicule (utile pour l'option parking).
     */
    public void save(Vehicule vehicule) {
        vehiculeRepository.save(vehicule);
    }

    public double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void suggererVehiculesProches(Utilisateur utilisateur, double rayonKm) {
        List<Vehicule> tousLesVehicules = vehiculeRepository.findAll();

        List<Vehicule> suggestions = tousLesVehicules.stream()
                .filter(v -> v.getEtat() == Vehicule.EtatVehicule.Non_loué)
                .filter(v -> {
                    double dist = calculerDistance(
                            utilisateur.getLatitudeHabitation(), utilisateur.getLongitudeHabitation(),
                            v.getLatitudeVehicule(), v.getLongitudeVehicule());
                    return dist <= rayonKm;
                })
                .toList();

        if (suggestions.isEmpty()) {
            System.out.println("Aucun véhicule trouvé dans un rayon de " + rayonKm + " km.");
        } else {
            System.out.println("\n--- SUGGESTIONS PROCHES DE CHEZ VOUS ---");
            suggestions.forEach(v -> {
                double d = calculerDistance(utilisateur.getLatitudeHabitation(), utilisateur.getLongitudeHabitation(),
                        v.getLatitudeVehicule(), v.getLongitudeVehicule());
                System.out.printf("- %s %s (à %.2f km)%n", v.getMarque(), v.getModele(), d);
            });
        }
    }
}
