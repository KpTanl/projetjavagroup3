package com.group3.carrental.entity;

import com.group3.carrental.entity.Vehicule;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCatalogue {

    public List<Vehicule> rechercherVehicules(List<Vehicule> catalogue, String ville, LocalDate dateRecherchee) {
        return catalogue.stream()
                .filter(v -> v.getVilleLocalisation().equalsIgnoreCase(ville))
                .filter(v -> v.getDatesDisponibles().contains(dateRecherchee))
                .collect(Collectors.toList());
    }

    public void consulterProfilEtVehicules(Agent agent) {
    System.out.println("========== PROFIL AGENT ==========");
    System.out.println("Nom : " + agent.getPrenom() + " " + agent.getNom());
    System.out.println("Email : " + agent.getEmail());
    
    // Calcul de la note moyenne de l'agent si tu as une classe NoteAgent
    System.out.println("Statut : " + agent.getRole()); 
    System.out.println("----------------------------------");
    System.out.println("VÉHICULES PROPOSÉS AU CATALOGUE :");

    if (agent.getVehiculesEnLocation() == null || agent.getVehiculesEnLocation().isEmpty()) {
        System.out.println("Cet agent ne propose aucun véhicule pour le moment.");
    } else {
        for (Vehicule v : agent.getVehiculesEnLocation()) {
            System.out.println("- " + v.getMarque() + " " + v.getModele() + " [" + v.getVilleLocalisation() + "]");
        }
    }
    System.out.println("==================================\n");
    }
}