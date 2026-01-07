package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
public abstract class Agent extends Utilisateur {
    private List<Integer> notesRecues;
    private LocalDate dateRecuFacture;
    private RoleAgent role;

    public enum RoleAgent {
        AgentPro,
        AgentParticulier
    }

    public Agent(int id, String nom, String prenom, String email, String motDePasse, List<Integer> notesRecues, LocalDate dateRecuFacture2) {
        super(id, nom, prenom, email, motDePasse, Role.Agent);
        this.notesRecues = notesRecues;
        this.dateRecuFacture = dateRecuFacture2;
        // TODO Auto-generated constructor stub
    }

    public void ajouterVehicule(Vehicule v) { /* ... */ }
    // public void souscrireOption(OptionPayante opt) { 
    //     this.optionsActives.add(opt); 
    // }
    // public abstract void accepterContrat(Contrat c);

    public void souscrireOption() {

    }

    public void accepterContrat() {

    }

    public void recevoirFacture(Date dateRecuFacture) {

    }

    public void proposerLieuDepose() {
        
    }
}
