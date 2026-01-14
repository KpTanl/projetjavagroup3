package com.group3.carrental.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.service.ContratService;
import com.group3.carrental.service.VehiculeService;

@Component
public class AgentController {
    private static final Scanner sc = new Scanner(System.in);
    private final VehiculeService vehiculeService;
    private final ContratService contratService;

    @Autowired
    public AgentController(VehiculeService vehiculeService, ContratService contratService) {
        this.vehiculeService = vehiculeService;
        this.contratService = contratService;
    }

    /**
     * Consulter l'historique des locations pour les véhicules de l'agent.
     * L'agent ne peut consulter que ses propres véhicules.
     */
    public void consulterHistoriqueVehicules(Utilisateur currentUser) {
        if (!(currentUser instanceof Agent)) {
            System.out.println("Accès refusé : vous devez être un Agent.");
            return;
        }

        // Afficher les véhicules de l'agent
        System.out.println("\n=== Mes Véhicules ===");
        List<Vehicule> mesVehicules = vehiculeService.getVehiculesByAgentId(currentUser.getId());

        if (mesVehicules.isEmpty()) {
            System.out.println("Vous n'avez aucun véhicule.");
            return;
        }

        for (Vehicule v : mesVehicules) {
            System.out.println(v.getId() + ". " + v.getMarque() + " " + v.getModele() + " (" + v.getCouleur() + ")");
        }

        System.out.print("\nEntrez l'ID du véhicule pour voir son historique : ");
        int vehiculeId = sc.nextInt();
        sc.nextLine();

        // Vérifier que le véhicule appartient à l'agent
        Vehicule vehiculeChoisi = mesVehicules.stream()
                .filter(v -> v.getId() == vehiculeId)
                .findFirst()
                .orElse(null);

        if (vehiculeChoisi == null) {
            System.out.println("Ce véhicule ne vous appartient pas ou n'existe pas.");
            return;
        }

        // Récupérer l'historique des contrats pour ce véhicule
        List<Contrat> contrats = contratService.getHistoriqueVehiculedeAgent(currentUser.getId(), vehiculeId);

        if (contrats.isEmpty()) {
            System.out.println("\nAucun historique de location pour ce véhicule.");
            return;
        }

        System.out.println("\n=== Historique du véhicule : " + vehiculeChoisi.getMarque() + " "
                + vehiculeChoisi.getModele() + " ===");
        for (Contrat c : contrats) {
            LocalDate deb = c.getDateDeb().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fin = c.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("\nContrat #" + c.getId());
            System.out.println("  Période    : du " + deb + " au " + fin);
            if (c.getLoueur() != null) {
                System.out.println("  Loueur     : " + c.getLoueur().getPrenom() + " " + c.getLoueur().getNom());
            }
            System.out.println("  Prix total : " + c.getPrixTotal() + "€");
            System.out.println("  Statut     : " + (c.getStatut() != null ? c.getStatut() : "Non défini"));
            System.out.println("------------------------------------");
        }
    }
}
