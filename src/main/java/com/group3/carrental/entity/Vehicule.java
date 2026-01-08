package com.group3.carrental.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Notes reçues - stockées dans une table séparée vehicule_notes
    // Notes reçues
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteVehicule> notesRecues = new ArrayList<>();

    // Disponibilités
    @ElementCollection
    @CollectionTable(name = "vehicule_disponibilites", joinColumns = @JoinColumn(name = "vehicule_id"))
    @Column(name = "date_disponible")
    private List<LocalDate> datesDisponibles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EtatVehicule etat;

    public enum EtatVehicule {
        Loué,
        Non_loué, Loue
    }

    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    public enum TypeVehicule {
        Voiture,
        Camion,
        Moto
    }

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public Vehicule(TypeVehicule type, String marque, String modele, String couleur, EtatVehicule etat,
            String rueLocalisation, String cPostalLocalisation, String villeLocalisation) {
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.etat = etat;
        this.rueLocalisation = rueLocalisation;
        this.cPostalLocalisation = cPostalLocalisation;
        this.villeLocalisation = villeLocalisation;
    }

    // --- METHODES ---

    public String getLocalisationComplete() {
        return rueLocalisation + ", " + cPostalLocalisation + " " + villeLocalisation;
    }

    public void ajouterDisponibilite(LocalDate date) {
        this.datesDisponibles.add(date);
    }

    public void ajouterNote(NoteVehicule note) {
        this.notesRecues.add(note);
        note.setVehicule(this);
    }

    // Calcule la note moyenne de toutes les évaluations
    public double calculerNoteMoyenne() {
        if (notesRecues == null || notesRecues.isEmpty()) {
            return 0.0;
        }
        return notesRecues.stream()
                .mapToDouble(NoteVehicule::calculerNoteGlobale)
                .average()
                .orElse(0.0);
    }
}