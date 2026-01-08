package com.group3.carrental.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Loueur extends Utilisateur {

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

    public Vehicule rechercherVehicule() {
        return null;
    }

    public void choisirDate(List<Date> d) {
    }

    public void choisirAssurance(List<Assurance> a) {
    }
}
