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
    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parkingPartenaire;

    // Notes reçues - stockées dans une table séparée vehicule_notes
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteVehicule> notesRecues = new ArrayList<>();

    // Disponibilites - EAGER: charger immediatement pour eviter
    // LazyInitializationException
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicule_disponibilites", joinColumns = @JoinColumn(name = "vehicule_id"))
    @Column(name = "date_disponible")
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
    @Enumerated(EnumType.STRING)
    private OptionRetour optionRetour;

    public enum OptionRetour {
        retour_parking ,
        retour_classique

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

    public String getLocalisationComplete() {
        return rueLocalisation + ", " + cPostalLocalisation + " " + villeLocalisation;
    }

    public void setParkingPartenaire(Parking parking) {
    this.parkingPartenaire = parking;
}
    public Parking getParkingPartenaire() {
    return this.parkingPartenaire;
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

    // Calcule la note moyenne de toutes les évaluations
    public double calculerNoteMoyenne() {
        if (notesRecues == null || notesRecues.isEmpty()) {
            return 0.0;
        }
        double noteMoyenne = notesRecues.stream()
                .mapToDouble(NoteVehicule::calculerNoteGlobale)
                .average()
                .orElse(0.0);
        noteMoyenne = Math.round(noteMoyenne * 100.0) / 100.0;
        return noteMoyenne;
    }
    public String getInfosRetourParking() {
    if (this.parkingPartenaire == null) return "Retour classique en rue.";

    Parking p = this.parkingPartenaire;
    // On utilise getLocalisationComplete() au lieu de tout réécrire
   // 3. Retour de la chaîne concaténée (bien fermer la ligne)
    return "DEPOSER AU : " + p.getLocalisationComplete() + "\nCONTRAINTES : " + p.getContraintes();
}
}