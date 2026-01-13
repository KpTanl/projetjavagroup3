package com.group3.carrental.entity;
import java.time.LocalDate;
import com.group3.carrental.entity.Vehicule.OptionRetour;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;




@Entity
@DiscriminatorValue("Agent")
public abstract class Agent extends Utilisateur {

    // EAGER: charger immediatement pour eviter LazyInitializationException
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<NoteAgent> notesRecues;

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

    public Agent(int id, String nom, String prenom, String email, String motDePasse, 
             List<NoteAgent> notesRecues, LocalDate dateRecuFacture2, 
             double latitudeHabitation, double longitudeHabitation) { // <--- Ajoute les deux paramètres ici
             
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
// Dans Agent.java, vérifiez que vous avez bien ceci :
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
            System.out.println("Option parking activée pour le : " + v.getType() +v.getModele()+ v.getMarque()+ v.getCouleur());
        } else {
            v.setOptionRetour(Vehicule.OptionRetour.retour_classique);
            System.out.println("Option parking desactivée pour le : " + v.getType() + v.getModele()+ v.getMarque()+ v.getCouleur());
        }
    } else {
        System.out.println("Action non autorisée sur ce véhicule.");
    }
}


    @OneToMany(mappedBy = "agentProprietaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicule> vehiculesEnLocation;

    public List<Vehicule> getVehiculesEnLocation() {
        return vehiculesEnLocation;
    }
}
