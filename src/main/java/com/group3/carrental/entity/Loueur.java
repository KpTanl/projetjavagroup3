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
public class Loueur extends Utilisateur implements Commun {

    @Transient
    private List<Contrat> historiqueLocations;

    // EAGER: charger immediatement pour eviter LazyInitializationException
    @OneToMany(mappedBy = "loueur", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteLoueur> notesRecues;

    public Loueur(int id, String nom, String prenom, String email, String motDePasse,
            List<Contrat> historiqueLocations, List<NoteLoueur> notesRecues,
            double latitudeHabitation, double longitudeHabitation) {
        super(id, nom, prenom, email, motDePasse, Role.Loueur, latitudeHabitation, longitudeHabitation);
        this.historiqueLocations = historiqueLocations;
        this.notesRecues = notesRecues;
    }

    // 构造函数（不带坐标 - kepeng版本兼容DataInitializer）
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
            System.out.println(
                    "Vérifiez l'orthographe ou choisissez une ville dans la liste des destinations possibles.");
        } else {
            System.out.println(resultats.size() + " parking(s) trouvé(s) à " + ville);
        }

        return resultats;
    }

    public void choisirEtDeposer(Vehicule v, Parking p) {
        // L'assignation se fait via la méthode de l'objet Parking
        boolean succes = p.ajouterVehicule(v);

        if (succes) {
            System.out.println("Assignation réussie au parking : " + p.getIdP());
        } else {
            System.out.println("Echec de l'assignation (Parking complet ou option non activée)");
        }
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
