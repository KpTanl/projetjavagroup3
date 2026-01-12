package com.group3.carrental.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Loueur extends Utilisateur implements Commun {

    @Transient
    private List<Contrat> historiqueLocations;

    // EAGER: charger immediatement pour eviter LazyInitializationException
    @OneToMany(mappedBy = "loueur", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteLoueur> notesRecues;

    public Loueur(int id, String nom, String prenom, String email, String motDePasse,
            List<Contrat> historiqueLocations, List<NoteLoueur> notesRecues) {
        super(id, nom, prenom, email, motDePasse, Role.Loueur);
        this.historiqueLocations = historiqueLocations;
        this.notesRecues = notesRecues;
    }

    @Override
    public Vehicule rechercherVehicule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vehicule FiltreVehicule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void ConsulterProfilsAgents() {
        // TODO Auto-generated method stub
    }

    public NoteVehicule noterVehicule() {
        return null;
    }

    public void choisirDate(List<Date> d) {

    }

    public void choisirAssurance(List<Assurance> a) {

    }

    public void ajouterNote(NoteLoueur note) {
        this.notesRecues.add(note);
        note.setLoueur(this);
    }

    public Double calculerNoteMoyenne() {
        if (notesRecues == null || notesRecues.isEmpty()) {
            return null;
        }
        double moyenne = notesRecues.stream()
                .mapToDouble(NoteLoueur::calculerNoteGlobale)
                .average()
                .orElse(0.0);
        return Math.round(moyenne * 100.0) / 100.0;
    }

}
