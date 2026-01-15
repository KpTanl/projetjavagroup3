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
    private final OptionPayanteAgentRepository optionPayanteAgentRepository;
    private final PrestataireEntretienRepository prestataireRepository;

    @Autowired
    public DataInitializer(VehiculeRepository vehiculeRepository,
                        UtilisateurRepository utilisateurRepository,
                        AssuranceRepository assuranceRepository,
                        EntrepriseRepository entrepriseRepository,
                        NoteAgentRepository noteAgentRepository,
                        NoteLoueurRepository noteLoueurRepository,
                        NoteVehiculeRepository noteVehiculeRepository,
                        ContratRepository contratRepository,
                        ParkingRepository parkingRepository,
                        OptionPayanteAgentRepository optionPayanteAgentRepository,
                        PrestataireEntretienRepository prestataireRepository) {
                this.vehiculeRepository = vehiculeRepository;
                this.utilisateurRepository = utilisateurRepository;
                this.assuranceRepository = assuranceRepository;
                this.entrepriseRepository = entrepriseRepository;
                this.noteAgentRepository = noteAgentRepository;
                this.noteLoueurRepository = noteLoueurRepository;
                this.noteVehiculeRepository = noteVehiculeRepository;
                this.contratRepository = contratRepository;
                this.parkingRepository = parkingRepository;
                this.optionPayanteAgentRepository = optionPayanteAgentRepository;
                this.prestataireRepository = prestataireRepository;
        }

    @Override
    public void run(String... args) {
        // 1. Parkings
        if (parkingRepository.count() == 0) {
            parkingRepository.save(new Parking("Parking_1_Paris", "Paris", "15 Rue de la Paix", "75002", 10, 15.0, 5.0, "Badge requis"));
            parkingRepository.save(new Parking("Parking1_Toulouse", "Toulouse", "Place du Capitole", "31000", 8, 10.0, 4.0, "Code : 31TL"));
        }

        // 2. Sécurité
        if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
            System.out.println("Données déjà présentes, arrêt de l'initialisation.");
            return;
        }

        // ========== Assurances ==========
        Assurance assuranceAZA = new Assurance("Assurance AZA Complète", "Voiture:30.0,Moto:45.0,Camion:60.0", 30.0);
        assuranceRepository.save(assuranceAZA);

        // ========== Loueurs ==========
        Loueur loueur1 = new Loueur(0, "Dupont", "Jean", "jean.dupont@email.com", "motdepasse123", new ArrayList<>(), new ArrayList<>(), 48.8584, 2.3488);
        Loueur loueur2 = new Loueur(0, "Martin", "Marie", "marie.martin@email.com", "password456", new ArrayList<>(), new ArrayList<>(), 48.8397, 2.2399);
        utilisateurRepository.save(loueur1);
        utilisateurRepository.save(loueur2);

        // ========== Agents ==========
        AgentPro agentPro1 = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123", new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro", 48.8566, 2.3522);
        AgentParticulier agentPart1 = new AgentParticulier(0, "Durand", "Paul", "paul.durand@email.com", "paulpass", new ArrayList<>(), LocalDate.now().minusDays(3), 48.8600, 2.3500);
        utilisateurRepository.save(agentPro1);
        utilisateurRepository.save(agentPart1);

        // ========== Véhicules ==========
        Vehicule v1 = new Vehicule(Vehicule.TypeVehicule.Voiture, "Renault", "Clio", "Bleu", Vehicule.EtatVehicule.Non_loué, "Rue de la Paix", "75000", "Paris", 48.8583, 2.2945);
        v1.setAgent(agentPro1);
        vehiculeRepository.save(v1);

        Vehicule v2 = new Vehicule(Vehicule.TypeVehicule.Moto, "Yamaha", "MT-07", "Noir", Vehicule.EtatVehicule.Non_loué, "Avenue des Minimes", "31000", "Toulouse", 48.8397, 2.2399);
        v2.setAgent(agentPart1);
        vehiculeRepository.save(v2);

        // ========== Contrats ==========
        Contrat c1 = new Contrat(Date.from(Instant.now().minus(10, ChronoUnit.DAYS)), Date.from(Instant.now().minus(6, ChronoUnit.DAYS)), agentPro1, loueur1, v1, 120.0);
        c1.setStatut(Contrat.Statut.Accepte);
        contratRepository.save(c1);

        Contrat c2 = new Contrat(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().minus(1, ChronoUnit.DAYS)), agentPart1, loueur2, v2, 150.0);
        c2.setStatut(Contrat.Statut.Accepte);
        contratRepository.save(c2);

        // ========== Notes (CORRIGÉES) ==========
        // On utilise c1 et c2 qui sont les variables définies juste au dessus
        noteVehiculeRepository.save(new NoteVehicule(4, 5, 4, "Très bon véhicule", v1, loueur1, c1));
        noteVehiculeRepository.save(new NoteVehicule(5, 5, 5, "Moto excellente", v2, loueur2, c2));

        noteAgentRepository.save(new NoteAgent(4, 5, 4, "Service pro", agentPro1, loueur1, c1));

        // ========== Options Payantes ==========
        OptionPayanteAgent opt1 = new OptionPayanteAgent("Mise en avant premium", 50.0f, agentPro1);
        opt1.souscrire();
        optionPayanteAgentRepository.save(opt1);

        // ========== Prestataires ==========
        prestataireRepository.save(new PrestataireEntretien("12345678900011", "CleanAuto", "Nettoyage"));
        prestataireRepository.save(new PrestataireEntretien("98765432100022", "FastRepair", "Réparations"));

        System.out.println("Initialisation réussie !");
    }
}