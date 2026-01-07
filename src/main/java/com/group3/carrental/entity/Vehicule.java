package com.group3.carrental.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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


    public Vehicule(int id, TypeVehicule type, String marque, String modele, String couleur, EtatVehicule etat, String rueLocalisation,
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