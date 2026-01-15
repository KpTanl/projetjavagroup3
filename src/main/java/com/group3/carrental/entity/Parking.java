package com.group3.carrental.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "parking")
public class Parking {
    // propriétés
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomP;
    private String VilleP;
    private String rueP;
    private String CodePostalP;
    private double prixstockage_jour;
    private double reductionloueur;
    private int nb_places_max;

    @OneToMany(mappedBy = "parkingPartenaire", fetch = FetchType.EAGER)
    private List<Vehicule> vehiculesGares = new ArrayList<>();

    private String contraintes;

    // Constructeurs
    public Parking(String nomP, String VilleP, String rueP, String CodePostalP, int nb_places_max,
            double prixstockage_jour, double reductionloueur, String contraintes) {
        this.nomP = nomP;
        this.VilleP = VilleP;
        this.rueP = rueP;
        this.CodePostalP = CodePostalP;
        this.nb_places_max = nb_places_max;
        this.prixstockage_jour = prixstockage_jour;
        this.reductionloueur = reductionloueur;
        this.contraintes = contraintes;
    }

    public String getLocalisationComplete() {
        // Concaténation simple des champs existants
        return this.rueP + ", " + this.CodePostalP + ", " + this.VilleP;
    }

    @Override
    public String toString() {
        return "Parking [" +
                "Ville='" + VilleP + '\'' +
                ", Rue='" + rueP + '\'' +
                ", CP='" + CodePostalP + '\'' +
                ", Prix Stockage/Jour=" + prixstockage_jour +
                ", Réduction Loueur=" + reductionloueur +
                ']';
    }

    public boolean ajouterVehicule(Vehicule v) {
        // 1. On vérifie d'abord si l'option est bien activée sur le véhicule
        if (v.getOptionRetour() != Vehicule.OptionRetour.retour_parking) {
            System.out.println("Erreur : Impossible d'associer ce véhicule. L'option parking n'est pas activée !");
            return false;
        }

        // 2. On vérifie la capacité du parking
        if (vehiculesGares.size() < nb_places_max) {
            vehiculesGares.add(v);
            v.setParkingPartenaire(this);
            return true;
        } else {
            System.out.println("Erreur : Le parking est complet !");
            return false;
        }
    }

    // Dans Parking.java
    public static List<String> getVillesDisponibles(List<Parking> tousLesParkings) {
        List<String> villes = new ArrayList<>();
        for (Parking p : tousLesParkings) {
            // On vérifie s'il y a de la place et si la ville n'est pas déjà dans la liste
            if (p.getVehiculesGares().size() < p.getNb_places_max() && !villes.contains(p.getVilleP())) {
                villes.add(p.getVilleP());
            }
        }
        return villes;
    }

    // À ajouter dans Parking.java
    public void afficherDetails() {
        System.out.println("\n-------------------------------------------");
        System.out.println("ADRESSE : " + getLocalisationComplete());
        System.out.println("CONTRAINTES : " + (contraintes != null ? contraintes : "Aucune"));
        System.out.println("INFOS : Parking partenaire Vienci - Durée longue autorisée");
        System.out.println("RÉDUCTION : -" + reductionloueur + "euros sur votre location");
        System.out.println("-------------------------------------------");
    }

}
