package com.group3.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// --- AJOUT DES IMPORTS MANQUANTS ---
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.GestionCatalogue;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
        
        System.out.println("\n--- Bienvenue sur notre application de location de véhicules ---");
        System.out.println("Groupe 3\n");

        // 1. Création d'un véhicule de test
        // Note: On passe null pour notesRecues et assurance pour l'instant
        Vehicule v1 = new Vehicule(1, "Renault", "Clio", "Bleu", "Rue de la Paix", "75000", "Paris", null, null);
        
        // 2. On lui ajoute une disponibilité pour demain
        LocalDate demain = LocalDate.now().plusDays(1);
        v1.ajouterDisponibilite(demain);

        // 3. On met nos véhicules dans une liste (notre catalogue)
        List<Vehicule> monCatalogue = List.of(v1);

        // 4. APPEL DU SERVICE DE RECHERCHE (Ce qui manquait)
        GestionCatalogue service = new GestionCatalogue();
        List<Vehicule> resultats = service.rechercherVehicules(monCatalogue, "Paris", demain);

        // 5. Affichage des informations demandées par l'US
        System.out.println("Résultats de la recherche pour Paris à la date : " + demain);
        if (resultats.isEmpty()) {
            System.out.println("Aucun véhicule disponible.");
        } else {
            for (Vehicule res : resultats) {
                System.out.println("------------------------------------");
                System.out.println("Véhicule trouvé : " + res.getMarque() + " " + res.getModele());
                System.out.println("Lieu : " + res.getLocalisationComplete());
                // System.out.println("Note moyenne : " + res.getNoteMoyenne() + "/5");
                System.out.println("------------------------------------");
            }
        }
    }
}