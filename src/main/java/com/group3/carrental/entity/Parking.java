package com.group3.carrental.entity;

import java.util.ArrayList;
import java.util.List;

public class Parking {
    //propriétés
    private String idP; 
    private String VilleP ;
    private String rueP ; 
    private String CodePostalP ; 
    private double prixstockage_jour;
    private double reductionloueur ;
    private int nb_places_max ; 
    private List<Vehicule> vehiculesGares = new ArrayList<>();
    private String contraintes;

    
    //Constructeurs
    public Parking(){};

    public Parking(String idP , String VilleP , String rueP, String CodePostalP,int nb_places_max,double prixstockage_jour , double reductionloueur, String contraintes){
        
        this.idP = idP;
        this.VilleP = VilleP;
        this.rueP = rueP ; 
        this.CodePostalP = CodePostalP ; 
        this.nb_places_max = nb_places_max;
        this.prixstockage_jour = prixstockage_jour;
        this.reductionloueur = reductionloueur ; 
        this.contraintes = contraintes ; 
    }
    //methodes (get/set + us)
    public String getidP() {
        return idP;
    }

    public String getVilleP() {
        return VilleP;
    }

    public String getRueP() {
        return rueP;
    }

    public String getCodePostalP() {
        return CodePostalP;
    }

    public double getPrixstockage_jour() {
        return prixstockage_jour;
    }

    public void setPrixstockage_jour(double prixstockage_jour) {
        this.prixstockage_jour = prixstockage_jour;
    }

    public double getReductionloueur() {
        return reductionloueur;
    }

    public int getNb_places_max() {
        return nb_places_max;
    }

    public List<Vehicule> getVehiculesGares() {
        return vehiculesGares;
    }
    public String getLocalisationComplete() {
    // Concaténation simple des champs existants
    return this.rueP + ", " + this.CodePostalP + ", " + this.VilleP;
}
public String getContraintes() {
    return this.contraintes;
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

    
}
