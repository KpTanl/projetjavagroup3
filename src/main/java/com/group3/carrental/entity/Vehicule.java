package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicule")
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
    private double latitudeVehicule;
    private double longitudeVehicule;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parkingPartenaire;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteVehicule> notesRecues = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicule_disponibilites", joinColumns = @JoinColumn(name = "vehicule_id"))
    @Column(name = "date_disponible")
    private List<LocalDate> datesDisponibles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EtatVehicule etat;

    public enum EtatVehicule {
        Loué, Non_loué, En_Entretien
    }

    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    public enum TypeVehicule {
        Voiture, Camion, Moto
    }

    @Enumerated(EnumType.STRING)
    private OptionRetour optionRetour;

    public enum OptionRetour {
        retour_parking, retour_classique
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id")
    private Agent agent;
    private int kilometrage;

    private LocalDate dateSuppressionPrevue;

    public Vehicule(TypeVehicule type, String marque, String modele, String couleur, EtatVehicule etat,
            String rueLocalisation, String cPostalLocalisation, String villeLocalisation, double latitudeVehicule,
            double longitudeVehicule) {
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.etat = etat;
        this.latitudeVehicule = latitudeVehicule;
        this.longitudeVehicule = longitudeVehicule;
        this.rueLocalisation = rueLocalisation;
        this.cPostalLocalisation = cPostalLocalisation;
        this.villeLocalisation = villeLocalisation;
    }

    // agentProprietaire est un alias pour agent (compatibilité riad2)
    public Agent getAgentProprietaire() {
        return this.agent;
    }

    public void setAgentProprietaire(Agent agent) {
        this.agent = agent;
    }

    public String getLocalisationComplete() {
        return rueLocalisation + ", " + cPostalLocalisation + " " + villeLocalisation;
    }

    public void ajouterDisponibilite(LocalDate date) {
        this.datesDisponibles.add(date);
    }

    public void ajouterNote(NoteVehicule note) {
        if (note == null)
            return;
        this.notesRecues.add(note);
        note.setVehicule(this);
    }

    public Double calculerNoteMoyenne() {
        if (notesRecues == null || notesRecues.isEmpty())
            return null;
        double noteMoyenne = notesRecues.stream()
                .mapToDouble(NoteVehicule::calculerNoteGlobale)
                .average()
                .orElse(0.0);
        return Math.round(noteMoyenne * 100.0) / 100.0;
    }

    public String getInfosRetourParking() {
        if (this.parkingPartenaire == null)
            return "Retour classique en rue.";
        Parking p = this.parkingPartenaire;
        return "DEPOSER AU : " + p.getLocalisationComplete() + "\nCONTRAINTES : " + p.getContraintes();
    }

}