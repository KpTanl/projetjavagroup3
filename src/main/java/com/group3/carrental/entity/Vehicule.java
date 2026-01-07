package com.group3.carrental.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import com.group3.carrental.service.NoteVehicule;

@Entity
@DiscriminatorValue("Vehicule")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String marque;
    private String modele;
    private String couleur;
    private String rueLocalisation;
    private String cPostalLocalisation;
    private String villeLocalisation;
    @OneToMany
    private List<NoteVehicule> notesRecues = new ArrayList<>();
    @OneToMany
    private Assurance assurance;
    private List<LocalDate> datesDisponibles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EtatVehicule etat;

    public enum EtatVehicule {
        Loué,
        Non_loué
    }

    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    public enum TypeVehicule {
        Voiture,
        Camion,
        Moto
    }

    public Vehicule(int id, TypeVehicule type, String marque, String modele, String couleur, EtatVehicule etat,
            String rueLocalisation,
            String cPostalLocalisation, String villeLocalisation, List<NoteVehicule> notesRecues, Assurance assurance) {
        this.id = id;
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.etat = etat;
        this.rueLocalisation = rueLocalisation;
        this.cPostalLocalisation = cPostalLocalisation;
        this.villeLocalisation = villeLocalisation;
        this.notesRecues = (notesRecues != null) ? notesRecues : new ArrayList<>();
        this.assurance = assurance;
    }

    // --- METHODES DE CONSULTATION ---

    // public double getNoteMoyenne() {
    // if (notesRecues == null || notesRecues.isEmpty()) return 0.0;
    // return notesRecues.stream()
    // .mapToDouble(NoteVehicule::getNoteGlobale)
    // .average()
    // .orElse(0.0);
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
    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getVilleLocalisation() {
        return villeLocalisation;
    }

    public String getNoteMoyenne() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNoteMoyenne'");
    }
}