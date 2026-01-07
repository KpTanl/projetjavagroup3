package com.group3.carrental.entity;

import java.util.ArrayList;
import java.util.List;
import com.group3.carrental.service.NoteVehicule;

public class Vehicule {
    private int id;
    private String marque;
    private String modele;
    private String couleur;
    private String rueLocalisation;
    private String cPostalLocalisation;
    private String villeLocalisation;
    private List<NoteVehicule> notesRecues = new ArrayList<>();
    private Assurance assurance;


    public Vehicule(int id, String marque, String modele, String couleur, String rueLocalisation,
            String cPostalLocalisation, String villeLocalisation, List<NoteVehicule> notesRecues, Assurance assurance) {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.rueLocalisation = rueLocalisation;
        this.cPostalLocalisation = cPostalLocalisation;
        this.villeLocalisation = villeLocalisation;
        this.notesRecues = notesRecues;
        this.assurance = assurance;
    }
    

    // public double calculerNoteMoyenne() {
    //     if(notesRecues.isEmpty()) return 0.0;
    //     return notesRecues.stream().mapToDouble(NoteVehicule::getNoteGlobale).average().orElse(0.0);
    // }

    // Getters pour type et modele
    // public String getType() { return type; }
    // public String getModele() { return modele; }
}