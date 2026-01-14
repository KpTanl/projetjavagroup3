package com.group3.carrental.donnee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
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
    private final ContratRepository contratRepository;
    private final ParkingRepository parkingRepository;

    @Autowired       
    public DataInitializer(VehiculeRepository vehiculeRepository,
                           UtilisateurRepository utilisateurRepository,
                           AssuranceRepository assuranceRepository,
                           EntrepriseRepository entrepriseRepository,
                           NoteAgentRepository noteAgentRepository,
                           NoteLoueurRepository noteLoueurRepository,
                           NoteVehiculeRepository noteVehiculeRepository,
                           ParkingRepository parkingRepository,
                           ContratRepository contratRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.assuranceRepository = assuranceRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.noteAgentRepository = noteAgentRepository;
        this.noteLoueurRepository = noteLoueurRepository;
        this.noteVehiculeRepository = noteVehiculeRepository;
        this.parkingRepository = parkingRepository;
        this.contratRepository = contratRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Initialisation des parkings
        if (parkingRepository.count() == 0) {
            System.out.println("Initialisation des parkings...");
            parkingRepository.save(new Parking("Parking_1_Paris", "Paris", "15 Rue de la Paix", "75002", 10, 15.0, 5.0, "Badge requis"));
            parkingRepository.save(new Parking("Parking1_Toulouse", "Toulouse", "Place du Capitole", "31000", 8, 10.0, 4.0, "Code : 31TL"));
        }

        // 2. Sécurité : On ne recrée pas tout si les données existent déjà
        if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
            System.out.println("Données déjà présentes, arrêt de l'initialisation.");
            return;
        }

        System.out.println("Initialisation des données de démonstration...");

        // ========== Assurances ==========
        Assurance assuranceAZA = new Assurance("Assurance AZA Complète", "Voiture:30.0,Moto:45.0,Camion:60.0", 30.0);
        assuranceRepository.save(assuranceAZA);
        Assurance assuranceConfort = new Assurance("Assurance Confort", "Voiture:50.0,Moto:65.0,Camion:85.0", 50.0);
        assuranceRepository.save(assuranceConfort);
        Assurance assurancePremium = new Assurance("Assurance Premium", "Voiture:80.0,Moto:100.0,Camion:120.0", 80.0);
        assuranceRepository.save(assurancePremium);

        // ========== Loueurs ==========
        Loueur loueur1 = new Loueur(0, "Dupont", "Jean", "jean.dupont@email.com", "motdepasse123", new ArrayList<>(), new ArrayList<>(), 48.8584, 2.3488);
        Loueur loueur2 = new Loueur(0, "Martin", "Marie", "marie.martin@email.com", "password456", new ArrayList<>(), new ArrayList<>(), 48.8397, 2.2399);
        Loueur loueur3 = new Loueur(0, "Bernard", "Lucie", "lucie.bernard@email.com", "luciepass", new ArrayList<>(), new ArrayList<>(), 48.8048, 2.1203);
        Loueur loueur4 = new Loueur(0, "Moreau", "Thomas", "thomas.moreau@email.com", "thomaspass", new ArrayList<>(), new ArrayList<>(), 48.4047, 2.7016);
        utilisateurRepository.save(loueur1);
        utilisateurRepository.save(loueur2);
        utilisateurRepository.save(loueur3);
        utilisateurRepository.save(loueur4);

        // ========== Agents ==========
        AgentPro agentPro1 = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123", new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro", 48.8566, 2.3522);
        AgentPro agentPro2 = new AgentPro(0, "AutoLoc", "Resp", "contact@autoloc.com", "autolocpass", new ArrayList<>(), LocalDate.now().minusDays(2), 98765432109876L, "AutoLoc Services", 45.7640, 4.8357);
        AgentPro agentPro3 = new AgentPro(0, "CityRent", "Mgr", "manager@cityrent.com", "cityrent123", new ArrayList<>(), LocalDate.now().minusWeeks(1), 11122233344455L, "CityRent Mobility", 43.2965, 5.3698);
        AgentParticulier agentPart1 = new AgentParticulier(0, "Durand", "Paul", "paul.durand@email.com", "paulpass", new ArrayList<>(), LocalDate.now().minusDays(3), 48.8600, 2.3500);
        AgentParticulier agentPart2 = new AgentParticulier(0, "Lefevre", "Camille", "camille.lefevre@email.com", "camillepass", new ArrayList<>(), LocalDate.now().minusWeeks(2), 44.8378, -0.5792);
        AgentParticulier agentPart3 = new AgentParticulier(0, "Petit", "Nicolas", "nicolas.petit@email.com", "nicolas123", new ArrayList<>(), LocalDate.now().minusMonths(1), 50.6292, 3.0573);
        utilisateurRepository.save(agentPro1);
        utilisateurRepository.save(agentPro2);
        utilisateurRepository.save(agentPro3);
        utilisateurRepository.save(agentPart1);
        utilisateurRepository.save(agentPart2);
        utilisateurRepository.save(agentPart3);

        // ========== Véhicules ==========
        Vehicule v1 = new Vehicule(Vehicule.TypeVehicule.Voiture, "Renault", "Clio", "Bleu", Vehicule.EtatVehicule.Non_loué, "Rue de la Paix", "75000", "Paris", 48.8583, 2.2945);
        v1.setAgent(agentPro1);
        vehiculeRepository.save(v1);

        Vehicule v2 = new Vehicule(Vehicule.TypeVehicule.Moto, "Yamaha", "MT-07", "Noir", Vehicule.EtatVehicule.Non_loué, "Avenue des Minimes", "31000", "Toulouse", 48.8397, 2.2399);
        v2.setAgent(agentPro2);
        vehiculeRepository.save(v2);

        Vehicule v3 = new Vehicule(Vehicule.TypeVehicule.Voiture, "Peugeot", "208", "Rouge", Vehicule.EtatVehicule.Non_loué, "Boulevard Carnot", "59000", "Lille", 48.9361, 2.3574);
        v3.setAgent(agentPro3);
        vehiculeRepository.save(v3);

        Vehicule v4 = new Vehicule(Vehicule.TypeVehicule.Camion, "Mercedes", "Sprinter", "Blanc", Vehicule.EtatVehicule.Non_loué, "Rue Nationale", "59800", "Lille", 48.8048, 2.1203);
        v4.setAgent(agentPart1);
        vehiculeRepository.save(v4);

        Vehicule v6 = new Vehicule(Vehicule.TypeVehicule.Voiture, "Toyota", "Yaris", "Blanc", Vehicule.EtatVehicule.Non_loué, "Rue Victor Hugo", "69000", "Lyon", 45.7640, 4.8357);
        v6.setAgent(agentPart1);
        vehiculeRepository.save(v6);

        // ========== Contrats ==========
        Date now = Date.from(Instant.now());
        Contrat c1 = new Contrat(Date.from(Instant.now().minus(10, ChronoUnit.DAYS)), Date.from(Instant.now().minus(6, ChronoUnit.DAYS)), agentPro1, loueur1, v1, 120.0);
        c1.setStatut(Contrat.Statut.Accepte);
        contratRepository.save(c1);

        Contrat c2 = new Contrat(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().minus(1, ChronoUnit.DAYS)), agentPart1, loueur1, v6, 150.0);
        c2.setStatut(Contrat.Statut.Accepte);
        contratRepository.save(c2);

        // ========== Notes ==========
        noteVehiculeRepository.save(new NoteVehicule(4, 5, 4, "Très bon véhicule", v1, loueur1, c1));
        noteVehiculeRepository.save(new NoteVehicule(5, 5, 5, "Super Yaris", v6, loueur1, c2));
        
        noteAgentRepository.save(new NoteAgent(4, 5, 4, "Agent sérieux", agentPro1, loueur1, c1));
        noteAgentRepository.save(new NoteAgent(5, 5, 5, "Paul est super", agentPart1, loueur1, c2));

        // ========== Entreprises ==========
        entrepriseRepository.save(new Entreprise(0, "clean@example.com", "pass", "CleanAuto", "12345", "Nettoyage"));
        entrepriseRepository.save(new Entreprise(0, "repair@example.com", "pass", "FastRepair", "67890", "Réparation"));

        System.out.println("Initialisation terminée avec succès !");
    }
}