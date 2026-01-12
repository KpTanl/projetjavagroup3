package com.group3.carrental.entity;


public class Parking {
    //propriétés
    private String idP ; 
    private String VilleP ;
    private String rueP ; 
    private String CodePostalP ; 
    private double prixstockage_jour;
    private double reductionloueur ;
    
    //Constructeurs
    public Parking(){};

    public Parking(String idP , String VilleP , String rueP, String CodePostalP,double prixstockage_jour , double reductionloueur){
        
        this.idP = idP;
        this.VilleP = VilleP;
        this.rueP = rueP ; 
        this.CodePostalP = CodePostalP ; 
        this.prixstockage_jour = prixstockage_jour;
        this.reductionloueur = reductionloueur ; 
    }
    //methodes (get/set + us)
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
}
