package com.group3.carrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "options_payantes_agent")
public class OptionPayanteAgent implements Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private float prixMensuel;
    private boolean estActive;

    @Temporal(TemporalType.DATE)
    private Date dateRenouvellement;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public OptionPayanteAgent(String type, float prixMensuel, Agent agent) {
        this.type = type;
        this.prixMensuel = prixMensuel;
        this.agent = agent;
        this.estActive = false;
    }

    @Override
    public void souscrire() {
        this.estActive = true;
        this.dateRenouvellement = new Date();
        System.out.println("[INFO] Option " + type + " activée pour l'agent " + agent.getNom());
    }

    @Override
    public void resilier() {
        this.estActive = false;
        System.out.println("[INFO] Option " + type + " résiliée.");
    }

    @Override
    public void renouveler() {
        if (this.estActive) {
            System.out.println("[INFO] Option " + type + " renouvelée.");
        }
    }
}
