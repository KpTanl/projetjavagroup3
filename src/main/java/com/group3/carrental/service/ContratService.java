package com.group3.carrental.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Loueur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.ContratRepository;

@Service
public class ContratService {

    private final ContratRepository contratRepository;

    public ContratService(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    public Contrat creerContrat(Date dateDebut, Date dateFin, Agent agent, Loueur loueur, Vehicule vehicule, double prixTotal) {
        Contrat contrat = new Contrat(dateDebut, dateFin, agent, loueur, vehicule, prixTotal);
        contrat.setStatut(Contrat.Statut.Accepte);
        return contratRepository.save(contrat);
    }

    public List<Contrat> getTousLesContrats() {
        return contratRepository.findAll();
    }

    public List<Contrat> getContratsParLoueur(int loueurId) {
        return contratRepository.findByLoueurId(loueurId);
    }

    public List<Contrat> getContratsParAgent(int agentId) {
        return contratRepository.findByAgentId(agentId);
    }

    public List<Contrat> getContratsTerminesAccepteesParLoueur(int loueurId) {
        return contratRepository.findByLoueurIdAndStatutAndDateFinBefore(
                loueurId, Contrat.Statut.Accepte, new Date());
    }

    public List<Contrat> getContratsTerminesAccepteesParAgent(int agentId) {
        return contratRepository.findByAgentIdAndStatutAndDateFinBefore(
                agentId, Contrat.Statut.Accepte, new Date());
    }

    /**
     * Récupérer un contrat par son ID
     */
    public Contrat getContratById(Long id) {
        return contratRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
    }

    /**
     * Supprimer un contrat
     */
    public void supprimerContrat(Long id) {
        contratRepository.deleteById(id);
    }

    public Contrat save(Contrat c) {
        return contratRepository.save(c);
    }

}
