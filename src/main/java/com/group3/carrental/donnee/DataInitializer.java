package com.group3.carrental.donnee;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.AgentPro;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Entreprise;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.NoteAgent;
import com.group3.carrental.entity.NoteVehicule;
import com.group3.carrental.entity.Parking;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.AssuranceRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
import com.group3.carrental.repository.NoteAgentRepository;
import com.group3.carrental.repository.NoteLoueurRepository;
import com.group3.carrental.repository.NoteVehiculeRepository;
import com.group3.carrental.repository.ParkingRepository;
import com.group3.carrental.repository.EntrepriseRepository;
import com.group3.carrental.entity.*;
import com.group3.carrental.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {

        private final VehiculeRepository vehiculeRepository;
        private final UtilisateurRepository utilisateurRepository;
        private final AssuranceRepository assuranceRepository;
        private final EntrepriseRepository entrepriseRepository;
        private final NoteAgentRepository noteAgentRepository;
        private final NoteLoueurRepository noteLoueurRepository;
        private final NoteVehiculeRepository noteVehiculeRepository;
        private final ParkingRepository parkingRepository;

        @Autowired
        public DataInitializer(VehiculeRepository vehiculeRepository,
                        UtilisateurRepository utilisateurRepository,
                        AssuranceRepository assuranceRepository,
                        EntrepriseRepository entrepriseRepository,
                        NoteAgentRepository noteAgentRepository,
                        NoteLoueurRepository noteLoueurRepository,
                        NoteVehiculeRepository noteVehiculeRepository, ParkingRepository parkingRepository) {
                this.vehiculeRepository = vehiculeRepository;
                this.utilisateurRepository = utilisateurRepository;
                this.assuranceRepository = assuranceRepository;
                this.entrepriseRepository = entrepriseRepository;
                this.noteAgentRepository = noteAgentRepository;
                this.noteLoueurRepository = noteLoueurRepository;
                this.noteVehiculeRepository = noteVehiculeRepository;
                this.parkingRepository = parkingRepository;

        }

        @Override
        public void run(String... args) {
                // 1. On initialise les parkings s'il n'y en a pas encore
    if (parkingRepository.count() == 0) {
        System.out.println("Initialisation des parkings...");
        Parking parkingParis = new Parking("Parking_1_Paris", "Paris", "15 Rue de la Paix", "75002", 10, 15.0, 5.0, "Badge requis");
        Parking parkingLyon = new Parking("Parking1_Lyon", "Lyon", "Place Bellecour", "69002", 5, 12.0, 3.0, "Code : 45A9");
        
        parkingRepository.save(parkingParis);
        parkingRepository.save(parkingLyon);
    }

    // 2. On garde ta sécurité pour le reste des données
    if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
        System.out.println("Autres données déjà présentes, suite de l'initialisation ignorée.");
        return;
    }

                System.out.println("Initialisation des données de démonstration...");

                // ========== Assurances (avec prix) ==========
                Assurance assuranceAZA = new Assurance(
                                "Assurance AZA Complète",
                                "Voiture:30.0,Moto:45.0,Camion:60.0,Voiture-Clio:28.0",
                                30.0);
                assuranceRepository.save(assuranceAZA);

                Assurance assuranceConfort = new Assurance(
                                "Assurance Confort",
                                "Voiture:50.0,Moto:65.0,Camion:85.0",
                                50.0);
                assuranceRepository.save(assuranceConfort);

                Assurance assurancePremium = new Assurance(
                                "Assurance Premium",
                                "Voiture:80.0,Moto:100.0,Camion:120.0",
                                80.0);
                assuranceRepository.save(assurancePremium);



                // ========== Utilisateurs ==========
                Loueur loueur1 = new Loueur(0, "Dupont", "Jean", "jean.dupont@email.com", "motdepasse123",
                                new ArrayList<>(), new ArrayList<>());
                utilisateurRepository.save(loueur1);

                Loueur loueur2 = new Loueur(0, "Martin", "Marie", "marie.martin@email.com", "password456",
                                new ArrayList<>(), new ArrayList<>());
                utilisateurRepository.save(loueur2);

                Loueur loueur3 = new Loueur(
                                0, "Bernard", "Lucie", "lucie.bernard@email.com", "luciepass",
                                new ArrayList<>(), new ArrayList<>());
                utilisateurRepository.save(loueur3);

                Loueur loueur4 = new Loueur(
                                0, "Moreau", "Thomas", "thomas.moreau@email.com", "thomaspass",
                                new ArrayList<>(), new ArrayList<>());
                utilisateurRepository.save(loueur4);

                AgentPro agentPro1 = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123",
                                new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro");
                agentPro1.ajouterNote(new NoteAgent(4, 5, 4, "Très bon service", agentPro1));
                agentPro1.ajouterNote(new NoteAgent(5, 5, 5, "Suuuper", agentPro1));
                utilisateurRepository.save(agentPro1);

                AgentPro agentPro2 = new AgentPro(
                                0,
                                "AutoLoc",
                                "Responsable",
                                "contact@autoloc.com",
                                "autolocpass",
                                new ArrayList<>(),
                                LocalDate.now().minusDays(2),
                                98765432109876L,
                                "AutoLoc Services");
                utilisateurRepository.save(agentPro2);

                AgentPro agentPro3 = new AgentPro(
                                0,
                                "CityRent",
                                "Manager",
                                "manager@cityrent.com",
                                "cityrent123",
                                new ArrayList<>(),
                                LocalDate.now().minusWeeks(1),
                                11122233344455L,
                                "CityRent Mobility");
                utilisateurRepository.save(agentPro3);

                AgentParticulier agentParticulier1 = new AgentParticulier(
                                0,
                                "Durand",
                                "Paul",
                                "paul.durand@email.com",
                                "paulpass",
                                new ArrayList<>(),
                                LocalDate.now().minusDays(3));
                utilisateurRepository.save(agentParticulier1);

                AgentParticulier agentParticulier2 = new AgentParticulier(
                                0,
                                "Lefevre",
                                "Camille",
                                "camille.lefevre@email.com",
                                "camillepass",
                                new ArrayList<>(),
                                LocalDate.now().minusWeeks(2));
                utilisateurRepository.save(agentParticulier2);

                AgentParticulier agentParticulier3 = new AgentParticulier(
                                0,
                                "Petit",
                                "Nicolas",
                                "nicolas.petit@email.com",
                                "nicolas123",
                                new ArrayList<>(),
                                LocalDate.now().minusMonths(1));
                utilisateurRepository.save(agentParticulier3);


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

                Vehicule v3 = new Vehicule(
                                Vehicule.TypeVehicule.Voiture,
                                "Peugeot",
                                "208",
                                "Rouge",
                                Vehicule.EtatVehicule.Non_loué,
                                "Boulevard Carnot",
                                "59000",
                                "Lille");
                v3.ajouterDisponibilite(LocalDate.now().plusDays(3));
                v3.ajouterNote(new NoteVehicule(4, 4, 5, "Voiture confortable"));
                vehiculeRepository.save(v3);

                Vehicule v4 = new Vehicule(
                                Vehicule.TypeVehicule.Camion,
                                "Mercedes",
                                "Sprinter",
                                "Blanc",
                                Vehicule.EtatVehicule.Non_loué,
                                "Rue Nationale",
                                "59800",
                                "Lille");
                v4.ajouterDisponibilite(LocalDate.now().plusDays(5));
                v4.ajouterNote(new NoteVehicule(3, 4, 4, "Utile pour déménagement"));
                vehiculeRepository.save(v4);

                Vehicule v5 = new Vehicule(
                                Vehicule.TypeVehicule.Moto,
                                "Honda",
                                "CB500",
                                "Gris",
                                Vehicule.EtatVehicule.Non_loué,
                                "Rue Alsace Lorraine",
                                "33000",
                                "Bordeaux");
                v5.ajouterDisponibilite(LocalDate.now().plusDays(1));
                vehiculeRepository.save(v5);

                Vehicule v6 = new Vehicule(
                                Vehicule.TypeVehicule.Voiture,
                                "Toyota",
                                "Yaris",
                                "Blanc",
                                Vehicule.EtatVehicule.Non_loué,
                                "Rue Victor Hugo",
                                "69000",
                                "Lyon");
                v6.ajouterDisponibilite(LocalDate.now().plusDays(2));
                v6.setAgent(agentParticulier1);
                vehiculeRepository.save(v6);

                Vehicule v7 = new Vehicule(
                                Vehicule.TypeVehicule.Camion,
                                "Iveco",
                                "Daily",
                                "Bleu",
                                Vehicule.EtatVehicule.Non_loué,
                                "Avenue Jean Jaurès",
                                "13000",
                                "Marseille");
                v7.ajouterDisponibilite(LocalDate.now().plusDays(4));
                v7.setAgent(agentParticulier2);
                vehiculeRepository.save(v7);

                Vehicule v8 = new Vehicule(
                                Vehicule.TypeVehicule.Voiture,
                                "BMW",
                                "Serie 1",
                                "Noir",
                                Vehicule.EtatVehicule.Non_loué,
                                "Place Bellecour",
                                "69002",
                                "Lyon");
                v8.ajouterDisponibilite(LocalDate.now().plusDays(3));
                v8.setAgent(agentParticulier3);
                vehiculeRepository.save(v8);

                // ========== Entreprises ==========
                Entreprise e1 = new Entreprise(0, "cleanauto@example.com", "pass123", "CleanAuto", "12345678900011",
                                "Nettoyage intérieur/extérieur");
                Entreprise e2 = new Entreprise(0, "fastrepair@example.com", "pass123", "FastRepair", "98765432100022",
                                "Réparations rapides");
                Entreprise e3 = new Entreprise(0, "autoservice@example.com", "pass123", "AutoService+",
                                "45678912300033", "Entretien complet");
                Entreprise e4 = new Entreprise(0, "progarage@example.com", "pass123", "ProGarage", "74185296300044",
                                "Garage professionnel");
                entrepriseRepository.save(e1);
                entrepriseRepository.save(e2);
                entrepriseRepository.save(e3);
                entrepriseRepository.save(e4);

                System.out.println("    Données de démonstration initialisées :");
                System.out.println("   - 3 assurances (AZA 30€/j, Confort 50€/j, Premium 80€/j)");
                System.out.println("   - 8 véhicules");
                System.out.println("   - 4 loueurs + 6 agents");
                System.out.println("   - 4 entreprises");
      
// ========== PARKING (BIEN PLACÉ ICI) ==========
 // Dans DataInitializer.java
Parking parkingParis = new Parking("Parking_1_Paris",
     "Paris", "15 Rue de la Paix", "75002", 
    10, 15.0, 5.0, "Badge requis à l'entrée, niveau -1"
);

Parking parkingLyon = new Parking(
     "Parking1_Lyon","Lyon", "Place Bellecour", "69002", 
    5, 12.0, 3.0, "Code portail : 45A9, place 12"
);

parkingRepository.save(parkingParis);
parkingRepository.save(parkingLyon);
  }
}