package com.group3.carrental.donnee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.repository.AssuranceRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
import com.group3.carrental.repository.NoteAgentRepository;
import com.group3.carrental.repository.NoteLoueurRepository;
import com.group3.carrental.repository.NoteVehiculeRepository;
import com.group3.carrental.repository.ParkingRepository;
import com.group3.carrental.repository.EntrepriseRepository;
import com.group3.carrental.repository.ContratRepository;
import com.group3.carrental.repository.OptionPayanteAgentRepository;
import com.group3.carrental.repository.PrestataireEntretienRepository;
import com.group3.carrental.entity.OptionPayanteAgent;
import com.group3.carrental.entity.PrestataireEntretien;

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

        /**
         * Ajoute disponibilité à un véhicule 
         */
        private void ajouterDisponibilite60Jours(Vehicule v) {
                LocalDate today = LocalDate.now();
                LocalDate endDate = today.plusDays(60);
                LocalDate current = today;
                while (!current.isAfter(endDate)) {
                        v.ajouterDisponibilite(current);
                        current = current.plusDays(1);
                }
        }

        @Override
        public void run(String... args) {
                // initialise les parkings 
                if (parkingRepository.count() == 0) {
                        System.out.println("Initialisation des parkings...");
                        Parking parkingParis = new Parking("Parking_1_Paris", "Paris", "15 Rue de la Paix", "75002", 10,
                                        15.0, 5.0, "Badge requis");
                        Parking parkingToulouse = new Parking("Parking1_Toulouse", "Toulouse", "Place du Capitole",
                                        "31000", 8, 10.0,
                                        4.0, "Code : 31TL");

                        parkingRepository.save(parkingParis);
                        parkingRepository.save(parkingToulouse);
                }

                // Securite des données
                if (vehiculeRepository.count() > 0 || utilisateurRepository.count() > 0) {
                        System.out.println("Autres données déjà présentes, suite de l'initialisation ignorée.");
                        return;
                }

                System.out.println("Initialisation des données de démonstration...");

                // Assurances 
                Assurance assuranceAZA = new Assurance(
                                "Assurance AZA Basique",
                                "Voiture:8.0,Moto:10.0,Camion:15.0,Voiture-Clio:7.0",
                                8.0);
                assuranceRepository.save(assuranceAZA);

                Assurance assuranceConfort = new Assurance(
                                "Assurance Confort",
                                "Voiture:12.0,Moto:15.0,Camion:20.0",
                                12.0);
                assuranceRepository.save(assuranceConfort);

                Assurance assurancePremium = new Assurance(
                                "Assurance Premium",
                                "Voiture:18.0,Moto:22.0,Camion:28.0",
                                18.0);
                assuranceRepository.save(assurancePremium);

                //  Utilisateurs (Loueurs) 
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

                // Utilisateurs (Agents Pro)
                AgentPro agentPro1 = new AgentPro(0, "Société", "Admin", "admin@rentcar.com", "admin123",
                                new ArrayList<>(), LocalDate.now(), 12345678901234L, "RentCar Pro", 48.8566, 2.3522);
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
                                "AutoLoc Services", 45.7640, 4.8357);
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
                                "CityRent Mobility", 43.2965, 5.3698);
                utilisateurRepository.save(agentPro3);

                //  Utilisateurs (Agents Particuliers) 
                AgentParticulier agentParticulier1 = new AgentParticulier(
                                0,
                                "Durand",
                                "Paul",
                                "paul.durand@email.com",
                                "paulpass",
                                new ArrayList<>(),
                                LocalDate.now().minusDays(3), 48.8600, 2.3500);
                utilisateurRepository.save(agentParticulier1);

                AgentParticulier agentParticulier2 = new AgentParticulier(
                                0,
                                "Lefevre",
                                "Camille",
                                "camille.lefevre@email.com",
                                "camillepass",
                                new ArrayList<>(),
                                LocalDate.now().minusWeeks(2), 44.8378, -0.5792);
                utilisateurRepository.save(agentParticulier2);

                AgentParticulier agentParticulier3 = new AgentParticulier(
                                0,
                                "Petit",
                                "Nicolas",
                                "nicolas.petit@email.com",
                                "nicolas123",
                                new ArrayList<>(),
                                LocalDate.now().minusMonths(1), 50.6292, 3.0573);
                utilisateurRepository.save(agentParticulier3);

                // Véhicules  
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
                ajouterDisponibilite60Jours(v1);
                v1.setAgent(agentPro1); // Assigné à agentPro1
                v1.setPrixJournalier(35.0); // 35€/jour
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
                ajouterDisponibilite60Jours(v2);
                v2.setAgent(agentPro2); // Assigné à agentPro2
                v2.setPrixJournalier(45.0); // 45€/jour (Moto)
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
                ajouterDisponibilite60Jours(v3);
                v3.setAgent(agentPro3); // Assigné à agentPro3
                v3.setPrixJournalier(40.0); // 40€/jour
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
                ajouterDisponibilite60Jours(v4);
                v4.setAgent(agentParticulier1); // Assigné à agentParticulier1
                v4.setPrixJournalier(80.0); // 80€/jour (Camion)
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
                ajouterDisponibilite60Jours(v5);
                v5.setPrixJournalier(50.0); // 50€/jour (Moto)
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
                ajouterDisponibilite60Jours(v6);
                v6.setAgent(agentParticulier1);
                v6.setPrixJournalier(30.0); // 30€/jour
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
                ajouterDisponibilite60Jours(v7);
                v7.setAgent(agentParticulier2);
                v7.setPrixJournalier(75.0); // 75€/jour (Camion)
                vehiculeRepository.save(v7);

                Vehicule v8 = new Vehicule(
                                Vehicule.TypeVehicule.Voiture,
                                "BMW",
                                "Serie 1",
                                "Noir",
                                Vehicule.EtatVehicule.Non_loué,
                                "Place Bellecour",
                                "69002",
                                "Lyon",
                                47.9029, 1.9092);
                ajouterDisponibilite60Jours(v8);
                v8.setAgent(agentParticulier3);
                v8.setPrixJournalier(55.0); // 55€/jour (BMW)
                vehiculeRepository.save(v8);

                // ========== Contrats pour démo terminés
                Date deb1 = Date.from(Instant.now().minus(10, ChronoUnit.DAYS));
                Date fin1 = Date.from(Instant.now().minus(6, ChronoUnit.DAYS));

                Date deb2 = Date.from(Instant.now().minus(15, ChronoUnit.DAYS));
                Date fin2 = Date.from(Instant.now().minus(11, ChronoUnit.DAYS));

                Date deb3 = Date.from(Instant.now().minus(20, ChronoUnit.DAYS));
                Date fin3 = Date.from(Instant.now().minus(16, ChronoUnit.DAYS));

                Date deb4 = Date.from(Instant.now().minus(5, ChronoUnit.DAYS));
                Date fin4 = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

                // Contrat 1: loueur1 loue v1 (agentPro1)
                Contrat contrat1 = new Contrat(deb1, fin1, agentPro1, loueur1, v1, 120.0);
                contrat1.setStatut(Contrat.Statut.Accepte);
                contratRepository.save(contrat1);

                // Contrat 2: loueur2 loue v2 (agentPro2)
                Contrat contrat2 = new Contrat(deb2, fin2, agentPro2, loueur2, v2, 180.0);
                contrat2.setStatut(Contrat.Statut.Accepte);
                contratRepository.save(contrat2);

                // Contrat 3: loueur3 loue v3 (agentPro3)
                Contrat contrat3 = new Contrat(deb3, fin3, agentPro3, loueur3, v3, 150.0);
                contrat3.setStatut(Contrat.Statut.Accepte);
                contratRepository.save(contrat3);

                // Contrat 4: loueur4 loue v4 (agentParticulier1)
                Contrat contrat4 = new Contrat(deb4, fin4, agentParticulier1, loueur4, v4, 200.0);
                contrat4.setStatut(Contrat.Statut.Accepte);
                contratRepository.save(contrat4);

                // Contrat 5: loueur1 loue v6 (agentParticulier1) - contratTermine original
                Date deb5 = Date.from(Instant.now().minus(5, ChronoUnit.DAYS));
                Date fin5 = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
                Contrat contratTermine = new Contrat(deb5, fin5, agentParticulier1, loueur1, v6, 150.0);
                contratTermine.setStatut(Contrat.Statut.Accepte);
                contratRepository.save(contratTermine);

                System.out.println("5 Contrats démo terminés créés.");

                // ========== NoteVehicule (7 paramètres requis : proprete, usure, confort,
                // commentaire,
                // vehicule, loueur, contrat) ==========
                NoteVehicule noteV1 = new NoteVehicule(4, 5, 4, "Très bon véhicule", v1, loueur1, contrat1);
                noteVehiculeRepository.save(noteV1);

                NoteVehicule noteV2 = new NoteVehicule(5, 5, 5, "Moto excellente", v2, loueur2, contrat2);
                noteVehiculeRepository.save(noteV2);

                NoteVehicule noteV3 = new NoteVehicule(4, 4, 5, "Voiture confortable", v3, loueur3, contrat3);
                noteVehiculeRepository.save(noteV3);

                NoteVehicule noteV4 = new NoteVehicule(3, 4, 4, "Utile pour déménagement", v4, loueur4, contrat4);
                noteVehiculeRepository.save(noteV4);

                System.out.println("4 Notes véhicule démo créées.");

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
                System.out.println("   - 2 parkings (Paris, Toulouse)");
                System.out.println("   - 5 contrats démo terminés");
                System.out.println("   - 4 notes véhicule + 2 notes agent");

                // ========== Options Payantes Agent (Démo) ==========
                OptionPayanteAgent opt1 = new OptionPayanteAgent("Mise en avant premium", 50.0f, agentPro1);
                opt1.souscrire(); // Active l'option
                optionPayanteAgentRepository.save(opt1);

                OptionPayanteAgent opt2 = new OptionPayanteAgent("Support prioritaire", 20.0f, agentPro1);
                // On ne l'active pas par défaut pour tester
                optionPayanteAgentRepository.save(opt2);

                System.out.println("   - 2 options payantes créées pour agentPro1");

                // ========== Prestataires d'Entretien ==========
                PrestataireEntretien p1 = new PrestataireEntretien("12345678900011", "CleanAuto",
                                "Nettoyage intérieur/extérieur");
                PrestataireEntretien p2 = new PrestataireEntretien("98765432100022", "FastRepair",
                                "Réparations rapides");
                PrestataireEntretien p3 = new PrestataireEntretien("45678912300033", "AutoService+",
                                "Entretien complet");
                PrestataireEntretien p4 = new PrestataireEntretien("74185296300044", "ProGarage",
                                "Garage professionnel");

                prestataireRepository.save(p1);
                prestataireRepository.save(p2);
                prestataireRepository.save(p3);
                prestataireRepository.save(p4);

                System.out.println("   - 4 prestataires d'entretien créés");
        }
}