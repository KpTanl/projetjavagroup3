package com.group3.carrental.entity;

import java.time.LocalDate;
import com.group3.carrental.entity.Vehicule.OptionRetour;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@DiscriminatorValue("Agent")
public abstract class Agent extends Utilisateur implements Commun {

    // Options payantes souscrites par l'agent
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OptionPayanteAgent> optionsSouscrites = new ArrayList<>();

    // EAGER: charger immediatement pour eviter LazyInitializationException
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteAgent> notesRecues;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vehicule> vehiculesEnLocation;

    @Column(name = "date_recu_facture")
    private LocalDate dateRecuFacture;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_agent")
    private RoleAgent role;

    public enum RoleAgent {
        AgentPro,
        AgentParticulier
    }

    protected Agent() {
    }

    public Agent(int id, String nom, String prenom, String email, String motDePasse, List<NoteAgent> notesRecues,
            LocalDate dateRecuFacture2, double latitudeHabitation, double longitudeHabitation) { // <--- Ajoute les deux
                                                                                                 // paramètres ici

        // Maintenant on peut les passer au parent (Utilisateur)
        super(id, nom, prenom, email, motDePasse, Role.Agent, latitudeHabitation, longitudeHabitation);

        this.notesRecues = notesRecues;
        this.dateRecuFacture = dateRecuFacture2;
    }

    public void souscrireOption() {

    }

    public void accepterContrat() {

    }

    public void recevoirFacture(Date dateRecuFacture) {

    }

    public void proposerLieuDepose() {

    }

    public void ajouterNote(NoteAgent note) {
        this.notesRecues.add(note);
        note.setAgent(this);
    }

    // Méthode pour activer l'option sur UN véhicule
    public void activerOptionParkingVehicule(Vehicule v) {
        if (v != null && v.getAgentProprietaire() != null && v.getAgentProprietaire().getId() == this.getId()) {
            // Accès via la classe Vehicule
            v.setOptionRetour(Vehicule.OptionRetour.retour_parking);
            System.out.println("Option parking activée pour le véhicule : " + v.getModele());
        } else {
            throw new IllegalStateException("Cet agent n'est pas autorisé à modifier ce véhicule.");
        }
    }

    // Méthode pour activer l'option sur PLUSIEURS véhicules (en masse)
    public void activerOptionParkingEnMasse(List<Vehicule> vehiculesSelectionnes) {
        for (Vehicule v : vehiculesSelectionnes) {
            activerOptionParkingVehicule(v);
        }
    }

    // Permet d'activer OU de désactiver selon le besoin
    public void configurerOptionParking(Vehicule v, boolean activer) {
        if (v != null && v.getAgentProprietaire() != null && v.getAgentProprietaire().getId() == this.getId()) {
            if (activer) {
                v.setOptionRetour(Vehicule.OptionRetour.retour_parking);
                System.out.println("Option parking activée pour le : " + v.getType() + v.getModele() + v.getMarque()
                        + v.getCouleur());
            } else {
                v.setOptionRetour(Vehicule.OptionRetour.retour_classique);
                System.out.println("Option parking desactivée pour le : " + v.getType() + v.getModele() + v.getMarque()
                        + v.getCouleur());
            }
        } else {
            System.out.println("Action non autorisée sur ce véhicule.");
        }
    }

    public Double calculerNoteMoyenne() {
        if (notesRecues == null || notesRecues.isEmpty()) {
            return null;
        }
        double moyenne = notesRecues.stream()
                .mapToDouble(NoteAgent::calculerNoteGlobale)
                .average()
                .orElse(0.0);
        return Math.round(moyenne * 100.0) / 100.0;
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

    public List<Vehicule> getVehiculesEnLocation() {
        if (this.vehiculesEnLocation == null) {
            this.vehiculesEnLocation = new ArrayList<>();
        }
        return vehiculesEnLocation;
    }

    public void setVehiculesEnLocation(List<Vehicule> vehiculesEnLocation) {
        this.vehiculesEnLocation = vehiculesEnLocation;
    }

    public List<OptionPayanteAgent> getOptionsSouscrites() {
        if (this.optionsSouscrites == null) {
            this.optionsSouscrites = new ArrayList<>();
        }
        return optionsSouscrites;
    }
}
