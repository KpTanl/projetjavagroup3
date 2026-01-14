package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
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
            double latitudeHabitation, double longitudeHabitation) {

        super(id, nom, prenom, email, motDePasse, notesRecues, dateRecuFacture,
                latitudeHabitation, longitudeHabitation);

        this.nSiret = nSiret;
        this.nomSociete = nomSociete;
    }

}
