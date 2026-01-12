package com.group3.carrental.donnee;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.AgentPro;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Entreprise;
import com.group3.carrental.entity.AgentParticulier;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.NoteAgent;
import com.group3.carrental.entity.NoteLoueur;
import com.group3.carrental.entity.NoteVehicule;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
import com.group3.carrental.repository.AssuranceRepository;
import com.group3.carrental.repository.NoteAgentRepository;
import com.group3.carrental.repository.NoteLoueurRepository;
import com.group3.carrental.repository.NoteVehiculeRepository;
import com.group3.carrental.repository.EntrepriseRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AssuranceRepository assuranceRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final NoteAgentRepository noteAgentRepository;
    private final NoteLoueurRepository noteLoueurRepository;
    private final NoteVehiculeRepository noteVehiculeRepository;


    public DataInitializer(VehiculeRepository vehiculeRepository, 
        UtilisateurRepository utilisateurRepository, 
        AssuranceRepository assuranceRepository,
        EntrepriseRepository entrepriseRepository,
        NoteAgentRepository noteAgentRepository,
        NoteLoueurRepository noteLoueurRepository,
        NoteVehiculeRepository noteVehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.assuranceRepository = assuranceRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.noteAgentRepository = noteAgentRepository;
        this.noteLoueurRepository = noteLoueurRepository;
        this.noteVehiculeRepository = noteVehiculeRepository;
    }

    @Override
    public void run(String... args) {
        if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
            System.out.println("Données existantes détectées, initialisation ignorée.");
            return;
        }

        System.out.println("Initialisation des données de démonstration...");

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
        v1.ajouterNote(new NoteVehicule(4, 5, 4, "Très bon véhicule", v1));
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
        v2.ajouterNote(new NoteVehicule(5, 5, 5, "Moto excellente", v2));
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
        v3.ajouterNote(new NoteVehicule(4, 4, 5, "Voiture confortable", v3));
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
        v4.ajouterNote(new NoteVehicule(3, 4, 4, "Utile pour déménagement", v4));
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
        vehiculeRepository.save(v8);

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


        AgentPro agentPro = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123",
                new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro");
        utilisateurRepository.save(agentPro);

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
                LocalDate.now().minusDays(3)
        );
        utilisateurRepository.save(agentParticulier1);

        AgentParticulier agentParticulier2 = new AgentParticulier(
                0,
                "Lefevre",
                "Camille",
                "camille.lefevre@email.com",
                "camillepass",
                new ArrayList<>(),
                LocalDate.now().minusWeeks(2)
        );
        utilisateurRepository.save(agentParticulier2);

        AgentParticulier agentParticulier3 = new AgentParticulier(
                0,
                "Petit",
                "Nicolas",
                "nicolas.petit@email.com",
                "nicolas123",
                new ArrayList<>(),
                LocalDate.now().minusMonths(1)
        );
        utilisateurRepository.save(agentParticulier3);

        Assurance a1 = new Assurance("Assurance Basic", "Couverture minimale");
        Assurance a2 = new Assurance("Assurance Standard", "Vol + dommages");
        Assurance a3 = new Assurance("Assurance Premium", "Tous risques");
        Assurance a4 = new Assurance("Assurance Pro", "Flotte professionnelle");
        assuranceRepository.save(a1);
        assuranceRepository.save(a2);
        assuranceRepository.save(a3);
        assuranceRepository.save(a4);


        Entreprise e1 = new Entreprise("12345678900011", "CleanAuto", "Nettoyage intérieur/extérieur");
        Entreprise e2 = new Entreprise("98765432100022", "FastRepair", "Réparations rapides");
        Entreprise e3 = new Entreprise("45678912300033", "AutoService+", "Entretien complet");
        Entreprise e4 = new Entreprise("74185296300044", "ProGarage", "Garage professionnel");
        entrepriseRepository.save(e1);
        entrepriseRepository.save(e2);
        entrepriseRepository.save(e3);
        entrepriseRepository.save(e4);

        Vehicule vehicule1 = vehiculeRepository.findAll().get(0);
        Vehicule vehicule2 = vehiculeRepository.findAll().get(1);
        Vehicule vehicule3 = vehiculeRepository.findAll().get(2);
        NoteVehicule nv1 = new NoteVehicule(4, 5, 4, "Très propre", vehicule1);

        NoteVehicule nv2 = new NoteVehicule(5, 5, 5, "Parfait état", vehicule1);

        NoteVehicule nv3 = new NoteVehicule(3, 4, 4, "Correct", vehicule2);

        NoteVehicule nv4 = new NoteVehicule(4, 4, 3, "Confort moyen", vehicule3);

        noteVehiculeRepository.save(nv1);
        noteVehiculeRepository.save(nv2);
        noteVehiculeRepository.save(nv3);
        noteVehiculeRepository.save(nv4);

        Loueur l1 = (Loueur) utilisateurRepository.findAll().get(0);
        Loueur l2 = (Loueur) utilisateurRepository.findAll().get(1);

        NoteLoueur nl1 = new NoteLoueur();
        nl1.setNoteTraitementVehicule(4);
        nl1.setNoteEngagement(5);
        nl1.setNoteResponsabilite(4);
        nl1.setCommentaire("Loueur sérieux");
        nl1.setLoueur(l1);
        
        NoteLoueur nl2 = new NoteLoueur();
        nl2.setNoteTraitementVehicule(5);
        nl2.setNoteEngagement(5);
        nl2.setNoteResponsabilite(5);
        nl2.setCommentaire("Excellent service");
        nl2.setLoueur(l1);

        NoteLoueur nl3 = new NoteLoueur();
        nl3.setNoteTraitementVehicule(3);
        nl3.setNoteEngagement(4);
        nl3.setNoteResponsabilite(3);
        nl3.setCommentaire("Correct");
        nl3.setLoueur(l2);

        noteLoueurRepository.save(nl1);
        noteLoueurRepository.save(nl2);
        noteLoueurRepository.save(nl3);

        AgentPro a5 = agentPro;
        AgentPro a6 = agentPro2;
        AgentPro a7 = agentPro3;

        AgentParticulier a8 = agentParticulier1;
        AgentParticulier a9 = agentParticulier2;
        AgentParticulier a10 = agentParticulier3;


        NoteAgent na1 = new NoteAgent();
        na1.setNoteGestionVehicule(4);
        na1.setNoteBienveillance(5);
        na1.setNoteReactivite(4);
        na1.setCommentaire("Agent professionnel et réactif");
        na1.setAgent(a5);

        NoteAgent na2 = new NoteAgent();
        na2.setNoteGestionVehicule(5);
        na2.setNoteBienveillance(5);
        na2.setNoteReactivite(5);
        na2.setCommentaire("Service excellent");
        na2.setAgent(a6);

        NoteAgent na3 = new NoteAgent();
        na3.setNoteGestionVehicule(3);
        na3.setNoteBienveillance(4);
        na3.setNoteReactivite(3);
        na3.setCommentaire("Correct mais peut mieux faire");
        na3.setAgent(a7);

        NoteAgent na4 = new NoteAgent();
        na4.setNoteGestionVehicule(4);
        na4.setNoteBienveillance(4);
        na4.setNoteReactivite(5);
        na4.setCommentaire("Très bonne réactivité");
        na4.setAgent(a8);

        NoteAgent na5 = new NoteAgent();
        na5.setNoteGestionVehicule(5);
        na5.setNoteBienveillance(4);
        na5.setNoteReactivite(4);
        na5.setCommentaire("Agent fiable");
        na5.setAgent(a9);

        NoteAgent na6 = new NoteAgent();
        na6.setNoteGestionVehicule(4);
        na6.setNoteBienveillance(5);
        na6.setNoteReactivite(5);
        na6.setCommentaire("Très apprécié par les clients");
        na6.setAgent(a10);

        noteAgentRepository.save(na1);
        noteAgentRepository.save(na2);
        noteAgentRepository.save(na3);
        noteAgentRepository.save(na4);
        noteAgentRepository.save(na5);
        noteAgentRepository.save(na6);

        System.out.println("    Données de démonstration initialisées :");
    }
}
