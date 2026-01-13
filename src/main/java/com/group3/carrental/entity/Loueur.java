package com.group3.carrental.entity;

import java.util.ArrayList;
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

    public NoteVehicule noterVehicule() {
        return null;
    }

    public void choisirDate(List<Date> d) {

    }

    public void choisirAssurance(List<Assurance> a) {

    }
    public List<Parking> consulterParkingsParVille(String ville, List<Parking> catalogue) {
    List<Parking> resultats = new ArrayList<>();
    
    for (Parking p : catalogue) {
        // Filtrage par ville (ignore la casse) et vérification de la capacité
        if (p.getVilleP().equalsIgnoreCase(ville) && p.getVehiculesGares().size() < p.getNb_places_max()) {
            resultats.add(p);
        }
    }

    // Gestion de l'erreur si aucun parking n'est trouvé
    if (resultats.isEmpty()) {
        System.out.println("--- AUCUN RÉSULTAT ---");
        System.out.println("La ville '" + ville + "' n'est pas disponible ou est complète.");
        System.out.println("Vérifiez l'orthographe ou choisissez une ville dans la liste des destinations possibles.");
    } else {
        System.out.println(resultats.size() + " parking(s) trouvé(s) à " + ville);
    }

    return resultats;
}
public void choisirEtDeposer(Vehicule v, Parking p) {
        // L'assignation se fait via la méthode de l'objet Parking
        boolean succes = p.ajouterVehicule(v); 

        if (succes) {
            System.out.println("Assignation réussie au parking : " + p.getidP());
        } else {
            System.out.println("Echec de l'assignation (Parking complet ou option non activée)");
        }
    }

}
