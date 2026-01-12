package com.group3.carrental.entity;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.group3.carrental.entity.NoteVehicule;
import com.group3.carrental.repository.Commun;


@Data
@Entity
@Table(name = "loueur")
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Loueur extends Utilisateur implements Commun{
    @OneToMany(mappedBy = "loueur", cascade = CascadeType.ALL)
    private List<Contrat> historiqueLocations;
    //list string n'est pas bon ca doit etre location apres rien n'est créer 
    @ElementCollection
    @CollectionTable(name = "loueur_notes_recues", joinColumns = @JoinColumn(name = "loueur_id"))
    private List<NoteLoueur> notesRecues;
    // la classe note agent n'est pas encore créer 

    public Loueur(int id, String nom, String prenom, String email, String motDePasse, List<Contrat> historiqueLocations, List<NoteLoueur> notesRecues) {
        super(id, nom, prenom, email, motDePasse, Role.Loueur);
        this.historiqueLocations = historiqueLocations;
        this.notesRecues = notesRecues;
        // TODO Auto-generated constructor stub
    }

    public Vehicule rechercherVehicule() {
        return null;
    }

   // public NoteVehicule noterVehicule() {
       // return null;
    //}

    public void choisirDate(List<Date> d) {

    }

    public void choisirAssurance(List<Assurance> a) {

    }

}
