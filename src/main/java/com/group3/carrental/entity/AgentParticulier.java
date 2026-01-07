package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AgentParticulier")
public class AgentParticulier extends Agent {

    public AgentParticulier(int id, String nom, String prenom, String email, String motDePasse,
            List<Integer> notesRecues, LocalDate dateRecuFacture) {
        super(id, nom, prenom, email, motDePasse, notesRecues, dateRecuFacture);
        //TODO Auto-generated constructor stub
    }

    
}
