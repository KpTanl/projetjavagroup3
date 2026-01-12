package com.group3.carrental.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.VehiculeRepository;

/**
 * Service pour gérer les opérations sur les véhicules.
 */
@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public VehiculeService(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
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
}
