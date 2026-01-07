package com.group3.carrental.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AgentPro")
public class AgentPro extends Agent {
    private long nSiret;
    private String nomSociete;

    public AgentPro(int id, String nom, String prenom, String email, String motDePasse,
                    List<Integer> notesRecues, LocalDate dateRecuFacture,
                    long nSiret, String nomSociete) {

        super(id, nom, prenom, email, motDePasse, notesRecues, dateRecuFacture);
        this.nSiret = nSiret;
        this.nomSociete = nomSociete;
    }


}
