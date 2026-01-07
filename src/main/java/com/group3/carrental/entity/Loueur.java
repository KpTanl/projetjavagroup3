package com.group3.carrental.entity;

import java.util.Date;
import java.util.List;
import com.group3.carrental.service.NoteVehicule;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Loueur")
public class Loueur extends Utilisateur {
    private List<String> historiqueLocations;
    private List<Integer> notesRecues;

    public Loueur(int id, String nom, String prenom, String email, String motDePasse, List<String> historiqueLocations, List<Integer> notesRecues) {
        super(id, nom, prenom, email, motDePasse, Role.Loueur);
        this.historiqueLocations = historiqueLocations;
        this.notesRecues = notesRecues;
        // TODO Auto-generated constructor stub
    }

    public Vehicule rechercherVehicule() {
        return null;
    }

    public NoteVehicule noterVehicule() {
        return null;
    }

    public void choisirDate(List<Date> d) {

    }

    public void choisirAssurance(List<Assurance> a) {

    }

}
