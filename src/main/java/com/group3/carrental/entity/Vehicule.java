package com.group3.carrental.entity;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
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
    private List<LocalDate> datesDisponibles = new ArrayList<>();

    public Vehicule(int id, String marque, String modele, String couleur, String rueLocalisation,
                    String cPostalLocalisation, String villeLocalisation, 
                    List<NoteVehicule> notesRecues, Assurance assurance) {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.rueLocalisation = rueLocalisation;
        this.cPostalLocalisation = cPostalLocalisation;
        this.villeLocalisation = villeLocalisation;
        this.notesRecues = (notesRecues != null) ? notesRecues : new ArrayList<>();
        this.assurance = assurance;
    }

    // --- METHODES DE CONSULTATION ---
    
    // public double getNoteMoyenne() {
    //     if (notesRecues == null || notesRecues.isEmpty()) return 0.0;
    //     return notesRecues.stream()
    //             .mapToDouble(NoteVehicule::getNoteGlobale)
    //             .average()
    //             .orElse(0.0);
    // }

    public String getLocalisationComplete() {
        return rueLocalisation + ", " + cPostalLocalisation + " " + villeLocalisation;
    }

    public List<LocalDate> getDatesDisponibles() {
        return datesDisponibles;
    }

    public void ajouterDisponibilite(LocalDate date) {
        this.datesDisponibles.add(date);
    }

    // --- GETTERS ---
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public String getCouleur() { return couleur; }
    public String getVilleLocalisation() { return villeLocalisation; }

    public String getNoteMoyenne() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNoteMoyenne'");
    }
}