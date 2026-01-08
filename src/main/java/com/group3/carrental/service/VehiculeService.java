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
                System.out.println("------------------------------------");
            }
        }
        System.out.println("Total vehicules en base: " + vehicules.size());
    }

    /**
     * Retourne tous les véhicules.
     */
    public List<Vehicule> getTousLesVehicules() {
        return vehiculeRepository.findAll();
    }
}
