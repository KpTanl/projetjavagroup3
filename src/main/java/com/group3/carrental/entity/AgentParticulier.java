package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AgentParticulier")
public class AgentParticulier extends Agent {

    public AgentParticulier() {
    }

    public AgentParticulier(int id, String nom, String prenom, String email, String motDePasse,
            List<NoteAgent> notesRecues, LocalDate dateRecuFacture,
            double latitudeHabitation, double longitudeHabitation) { // 1. Ajout ici

        // 2. Transmission au constructeur de la classe Agent
        super(id, nom, prenom, email, motDePasse, notesRecues, dateRecuFacture,
                latitudeHabitation, longitudeHabitation);
    }

}
