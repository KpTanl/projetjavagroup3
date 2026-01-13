package com.group3.carrental.entity;

import java.util.ArrayList;
import java.util.List;

public class Test_Parking {
    public static void main(String[] args) {
        
        // --- 1. PRÉPARATION DES DONNÉES ---
        List<Parking> catalogue = new ArrayList<>();
        catalogue.add(new Parking("P1", "Montpellier", "Rue Jean", "34000", 1, 10.0, 0.2, "Hauteur 1m90"));
        catalogue.add(new Parking("P2", "Paris", "Rue de la Paix", "75000", 5, 15.0, 0.1, "Aucune"));

        Loueur loueur = new Loueur();
        
        Vehicule v1 = new Vehicule(
            Vehicule.TypeVehicule.Voiture, 
            "Tesla", 
            "Model 3", 
            "Rouge", 
            Vehicule.EtatVehicule.Non_loué, 
            "Rue de la Paix", 
            "75000", 
            "Paris"
        );

        // --- 2. TESTS DE L'AGENT (US 1) ---
        v1.setOptionRetour(Vehicule.OptionRetour.retour_classique);
        Agent agent = new AgentParticulier(); 
        agent.setId(1); 
        v1.setAgent(agent);

        System.out.println("État initial : " + v1.getOptionRetour());
        agent.activerOptionParkingVehicule(v1);
        System.out.println("Après activation par l'agent : " + v1.getOptionRetour());

        // --- 3. TESTS DU LOUEUR (US 2 - TA NOUVELLE US) ---
        
        // ÉTAPE A : Voir les villes disponibles
        System.out.println("\n--- Villes disponibles au catalogue ---");
        System.out.println(Parking.getVillesDisponibles(catalogue));

        // ÉTAPE B : Le loueur cherche dans une ville (Test Erreur de frappe)
        System.out.println("\n--- Test Erreur de frappe ---");
        loueur.consulterParkingsParVille("Montpelié", catalogue);

        // ÉTAPE C : Le loueur cherche la bonne ville
        System.out.println("\n--- Test Recherche Réussie ---");
        List<Parking> options = loueur.consulterParkingsParVille("Montpellier", catalogue);

        // ÉTAPE D : Le loueur choisit le parking et dépose
        if (!options.isEmpty()) {
            Parking choix = options.get(0);
            loueur.choisirEtDeposer(v1, choix);
        }

        // ÉTAPE E : Vérification finale (P1 est plein, Montpellier doit disparaître)
        System.out.println("\n--- Test État des parkings après dépôt ---");
        List<String> villesApres = Parking.getVillesDisponibles(catalogue);
        System.out.println("Villes disponibles avec places libres : " + villesApres);
        

    } // Fin du main
} // Fin de la classeP