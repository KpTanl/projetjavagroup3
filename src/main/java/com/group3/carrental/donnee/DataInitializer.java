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
import com.group3.carrental.repository.AssuranceRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
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

                // ========== Véhicules ==========
                Vehicule v1 = new Vehicule(
                                Vehicule.TypeVehicule.Voiture,
                                "Renault",
                                "Clio",
                                "Bleu",
                                Vehicule.EtatVehicule.Non_loué,
                                "Rue de la Paix",
                                "75000",
                                "Paris",
                                48.8583, 2.2945);
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
                                "Toulouse",
                                48.8397, 2.2399);
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
                                "Lille",
                                48.9361, 2.3574);
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
                                "Lille",
                                48.8048, 2.1203);
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
                                "Bordeaux",
                                48.8674, 2.7836);
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
                                "Lyon",
                                45.7640, 4.8357);
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
                                "Marseille",
                                48.4047, 2.7016);
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
                                "Lyon", 47.9029, 1.9092);
                v8.ajouterDisponibilite(LocalDate.now().plusDays(3));
                vehiculeRepository.save(v8);

                // ========== Utilisateurs ==========
                Loueur loueur1 = new Loueur(0, "Dupont", "Jean", "jean.dupont@email.com", "motdepasse123",
                                new ArrayList<>(), new ArrayList<>(), 48.8584, 2.3488);
                utilisateurRepository.save(loueur1);

                Loueur loueur2 = new Loueur(0, "Martin", "Marie", "marie.martin@email.com", "password456",
                                new ArrayList<>(), new ArrayList<>(), 48.8397, 2.2399);
                utilisateurRepository.save(loueur2);

                Loueur loueur3 = new Loueur(
                                0, "Bernard", "Lucie", "lucie.bernard@email.com", "luciepass",
                                new ArrayList<>(), new ArrayList<>(), 48.8048, 2.1203);
                utilisateurRepository.save(loueur3);

                Loueur loueur4 = new Loueur(
                                0, "Moreau", "Thomas", "thomas.moreau@email.com", "thomaspass",
                                new ArrayList<>(), new ArrayList<>(), 48.4047, 2.7016);
                utilisateurRepository.save(loueur4);

                AgentPro agentPro1 = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123",
                                new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro", 48.8924, 2.2384);
                agentPro1.ajouterNote(new NoteAgent(4, 5, 4, "Très bon service", 4.33, agentPro1));
                agentPro1.ajouterNote(new NoteAgent(5, 5, 5, "Suuuper", 5.0, agentPro1));
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
                                "AutoLoc Services", 48.9575, 2.8931);
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
                                "CityRent Mobility", 47.9029, 1.9092);
                utilisateurRepository.save(agentPro3);

                AgentParticulier agentParticulier1 = new AgentParticulier(
                                0,
                                "Durand",
                                "Paul",
                                "paul.durand@email.com",
                                "paulpass",
                                new ArrayList<>(),
                                LocalDate.now().minusDays(3), 48.9361, 2.3574);
                utilisateurRepository.save(agentParticulier1);

                AgentParticulier agentParticulier2 = new AgentParticulier(
                                0,
                                "Lefevre",
                                "Camille",
                                "camille.lefevre@email.com",
                                "camillepass",
                                new ArrayList<>(),
                                LocalDate.now().minusWeeks(2), 48.8566, 2.3522);
                utilisateurRepository.save(agentParticulier2);

                AgentParticulier agentParticulier3 = new AgentParticulier(
                                0,
                                "Petit",
                                "Nicolas",
                                "nicolas.petit@email.com",
                                "nicolas123",
                                new ArrayList<>(),
                                LocalDate.now().minusMonths(1), 49.8566, 2.8522);
                utilisateurRepository.save(agentParticulier3);

                // ========== Entreprises ==========
                Entreprise e1 = new Entreprise("12345678900011", "CleanAuto", "Nettoyage intérieur/extérieur");
                Entreprise e2 = new Entreprise("98765432100022", "FastRepair", "Réparations rapides");
                Entreprise e3 = new Entreprise("45678912300033", "AutoService+", "Entretien complet");
                Entreprise e4 = new Entreprise("74185296300044", "ProGarage", "Garage professionnel");
                entrepriseRepository.save(e1);
                entrepriseRepository.save(e2);
                entrepriseRepository.save(e3);
                entrepriseRepository.save(e4);

                System.out.println("    Données de démonstration initialisées :");
                System.out.println("   - 3 assurances (AZA 30€/j, Confort 50€/j, Premium 80€/j)");
                System.out.println("   - 8 véhicules");
                System.out.println("   - 4 loueurs + 6 agents");
                System.out.println("   - 4 entreprises");
        }
}
