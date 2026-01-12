package com.group3.carrental.entity;

import java.time.LocalDate;
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
