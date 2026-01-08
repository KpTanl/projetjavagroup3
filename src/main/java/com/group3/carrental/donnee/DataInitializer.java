package com.group3.carrental.donnee;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.AgentPro;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.AssuranceRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
import com.group3.carrental.service.NoteVehicule;

/**
 * Initialise les données de démonstration au démarrage de l'application.
 * Vérifie si la base de données est vide avant d'insérer les données.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AssuranceRepository assuranceRepository;

    public DataInitializer(VehiculeRepository vehiculeRepository, 
                          UtilisateurRepository utilisateurRepository,
                          AssuranceRepository assuranceRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.assuranceRepository = assuranceRepository;
    }

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
            System.out.println("Données existantes détectées, initialisation ignorée.");
            return;
        }

        System.out.println("Initialisation des données de démonstration...");

        // ========== Assurances ==========
        // Nomenclature grille: "TYPE:PRIX,TYPE-MODELE:PRIX"
        
        // Assurance AZA par défaut - avec grille par type de véhicule
        Assurance assuranceAZA = new Assurance(
            "Assurance AZA Complète", 
            "Voiture:30.0,Moto:45.0,Camion:60.0,Voiture-Clio:28.0", 
            30.0
        );
        assuranceRepository.save(assuranceAZA);

        // Assurance Confort - prix uniformes
        Assurance assuranceConfort = new Assurance(
            "Assurance Confort", 
            "Voiture:50.0,Moto:65.0,Camion:85.0", 
            50.0
        );
        assuranceRepository.save(assuranceConfort);

        // Assurance Premium - tarifs premium
        Assurance assurancePremium = new Assurance(
            "Assurance Premium", 
            "Voiture:80.0,Moto:100.0,Camion:120.0", 
            80.0
        );
        assuranceRepository.save(assurancePremium);

        // ========== Véhicules ==========
        Vehicule v1 = new Vehicule(
                Vehicule.TypeVehicule.Voiture,
                "Renault",
                "Clio",
                "Bleu",
                Vehicule.EtatVehicule.Non_loué,
                "Rue de la Paix",
                "75000",
                "Paris");
        v1.ajouterDisponibilite(LocalDate.now().plusDays(1));
        v1.ajouterNote(new NoteVehicule(4, 5, 4, "Très bon véhicule"));
        vehiculeRepository.save(v1);

        Vehicule v2 = new Vehicule(
                Vehicule.TypeVehicule.Moto,
                "Yamaha",
                "MT-07",
                "Noir",
                Vehicule.EtatVehicule.Non_loué,
                "Avenue des Minimes",
                "31000",
                "Toulouse");
        v2.ajouterDisponibilite(LocalDate.now().plusDays(2));
        v2.ajouterNote(new NoteVehicule(5, 5, 5, "Moto excellente"));
        vehiculeRepository.save(v2);

        // ========== Utilisateurs ==========
        Loueur loueur1 = new Loueur(0, "Dupont", "Jean", "jean.dupont@email.com", "motdepasse123",
                new ArrayList<>(), new ArrayList<>());
        utilisateurRepository.save(loueur1);

        Loueur loueur2 = new Loueur(0, "Martin", "Marie", "marie.martin@email.com", "password456",
                new ArrayList<>(), new ArrayList<>());
        utilisateurRepository.save(loueur2);

        AgentPro agentPro = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123",
                new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro");
        utilisateurRepository.save(agentPro);

        System.out.println("    Données de démonstration initialisées :");
        System.out.println("   - 3 assurances (AZA 30€/j, Confort 50€/j, Premium 80€/j)");
        System.out.println("   - 2 véhicules (Paris, Toulouse)");
        System.out.println("   - 2 loueurs + 1 agent professionnel");
    }
}
