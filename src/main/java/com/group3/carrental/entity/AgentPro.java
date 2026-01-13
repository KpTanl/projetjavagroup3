package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AgentPro")
public class AgentPro extends Agent {
    private long nSiret;
    private String nomSociete;

    public AgentPro() {
    }

    public AgentPro(int id, String nom, String prenom, String email, String motDePasse,
            List<NoteAgent> notesRecues, LocalDate dateRecuFacture,
            long nSiret, String nomSociete,
            double latitudeHabitation, double longitudeHabitation) { // 1. Ajout ici

        // 2. On les passe au constructeur de Agent (qui doit lui aussi les accepter)
        super(id, nom, prenom, email, motDePasse, notesRecues, dateRecuFacture,
                latitudeHabitation, longitudeHabitation);

        this.nSiret = nSiret;
        this.nomSociete = nomSociete;
    }

}
