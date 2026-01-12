package com.group3.carrental.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.ContratRepository;
import com.group3.carrental.repository.VehiculeRepository;

/**
 * Service pour gérer les opérations sur les véhicules.
 */
@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ContratRepository contratRepository;

    @Autowired
    public VehiculeService(VehiculeRepository vehiculeRepository, ContratRepository contratRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.contratRepository = contratRepository;
    }

    /**
     * Affiche tous les véhicules dans la console.
     */
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
                System.out.println("Note moyenne: " + v.calculerNoteMoyenne() + "/5");
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
        
        // Filtrer pour garder seulement les véhicules avec des dates disponibles
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
                System.out.println("Note moyenne: " + v.calculerNoteMoyenne() + "/5");
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
        if (v == null) return null;
        return v.getEtat() == Vehicule.EtatVehicule.Non_loué ? v : null;
    }

    /**
     * Récupère les dates disponibles pour un véhicule spécifique en fonction des contrats
     * existants dans la base de données.
     * 
     * @param vehiculeId l'ID du véhicule
     * @return une liste des dates disponibles calculées à partir des contrats
     */
    public List<LocalDate> getDatesDisponiblesVehicule(int vehiculeId) {
        Vehicule vehicule = getVehiculeById(vehiculeId);
        if (vehicule == null) {
            return new ArrayList<>();
        }

        return calculerDatesDisponibles(vehicule);
    }

    /**
     * Calcule les dates disponibles pour un véhicule en fonction des contrats actifs.
     * Les dates indisponibles sont celles qui sont couvertes par un contrat existant.
     * 
     * @param vehicule le véhicule
     * @return la liste des dates disponibles
     */
    private List<LocalDate> calculerDatesDisponibles(Vehicule vehicule) {
        List<Contrat> contrats = contratRepository.findAll().stream()
                .filter(c -> c.getVehicule() != null && c.getVehicule().getId() == vehicule.getId())
                .collect(Collectors.toList());

        // Récupérer la plage de dates à considérer (par exemple, 1 an à partir d'aujourd'hui)
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        // Créer une liste de toutes les dates disponibles
        List<LocalDate> datesDisponibles = new ArrayList<>();
        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            datesDisponibles.add(date);
        }

        // Filtrer les dates réservées par les contrats
        for (Contrat contrat : contrats) {
            LocalDate dateDebLocal = contrat.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateFinLocal = contrat.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            datesDisponibles.removeIf(date -> !date.isBefore(dateDebLocal) && !date.isAfter(dateFinLocal));
        }

        return datesDisponibles;
    }

    /**
     * Affiche les dates disponibles pour chaque véhicule en les récupérant depuis la base de données.
     */
    public void afficherDatesDisponibles() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();

        System.out.println("\n--- Dates disponibles pour chaque véhicule ---");
        if (vehicules.isEmpty()) {
            System.out.println("Aucun véhicule en base de données.");
        } else {
            for (Vehicule v : vehicules) {
                List<LocalDate> datesDisponibles = calculerDatesDisponibles(v);
                System.out.println("------------------------------------");
                System.out.println("Véhicule ID: " + v.getId() + " - " + v.getMarque() + " " + v.getModele());
                if (datesDisponibles.isEmpty()) {
                    System.out.println("Aucune date disponible");
                } else {
                    System.out.println("Nombre de jours disponibles: " + datesDisponibles.size());
                    System.out.println("Première date disponible: " + datesDisponibles.get(0));
                    System.out.println("Dernière date disponible: " + datesDisponibles.get(datesDisponibles.size() - 1));
                }
                System.out.println("------------------------------------");
                System.err.println("Note: Les dates affichées sont calculées en fonction des contrats existants.");
            }
        }
    }
}
