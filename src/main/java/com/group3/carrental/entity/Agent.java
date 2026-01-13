package com.group3.carrental.entity;
import java.time.LocalDate;
import com.group3.carrental.entity.Vehicule.OptionRetour;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Agent")
public abstract class Agent extends Utilisateur implements Commun {

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

    public Agent(int id, String nom, String prenom, String email, String motDePasse, List<NoteAgent> notesRecues,
            LocalDate dateRecuFacture2) {
        super(id, nom, prenom, email, motDePasse, Role.Agent);
        this.notesRecues = notesRecues;
        this.dateRecuFacture = dateRecuFacture2;
        // TODO Auto-generated constructor stub
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
    if (v != null && v.getAgent() != null && v.getAgent().getId() == this.getId()) {
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
    if (v != null && v.getAgent() != null && v.getAgent().getId() == this.getId()) {
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
}
