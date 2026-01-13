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

    public Contrat creerContrat(Date dateDeb, Date dateFin, Agent agent, Loueur loueur, Vehicule vehicule,
            double prixTotal) {
        Contrat contrat = new Contrat(dateDeb, dateFin, agent, loueur, vehicule, prixTotal);
        contrat.setStatut(Contrat.Statut.Presigne);
        return contratRepository.save(contrat);
    }

    public Contrat creerContratPresigne(Date dateDeb, Date dateFin,
            Agent agent, Loueur loueur,
            Vehicule vehicule, double prixTotal) {
        Contrat contrat = new Contrat(dateDeb, dateFin, agent, loueur, vehicule, prixTotal);
        contrat.setStatut(Contrat.Statut.Presigne);
        return contratRepository.save(contrat);
    }

    public Contrat accepterContrat(Long contratId, int agentId) {
        Contrat c = getContratById(contratId);

        if (c.getAgent() == null || c.getAgent().getId() != agentId) {
            throw new IllegalArgumentException("Ce contrat ne vous concerne pas.");
        }
        if (c.getStatut() != Contrat.Statut.Presigne) {
            throw new IllegalArgumentException("Contrat non pré-signé.");
        }

        c.setStatut(Contrat.Statut.Accepte);
        return contratRepository.save(c);
    }

    public Contrat refuserContrat(Long contratId, int agentId) {
        Contrat c = getContratById(contratId);

        if (c.getAgent() == null || c.getAgent().getId() != agentId) {
            throw new IllegalArgumentException("Ce contrat ne vous concerne pas.");
        }
        if (c.getStatut() != Contrat.Statut.Presigne) {
            throw new IllegalArgumentException("Contrat non pré-signé.");
        }

        c.setStatut(Contrat.Statut.Refuse);
        return contratRepository.save(c);
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

    public List<Contrat> getContratsParVehicule(int vehiculeId) {
        return contratRepository.findByVehiculeId(vehiculeId);
    }

    public List<Contrat> getContratsParDate(Date dateDeb, Date dateFin) {
        return contratRepository.findByDateDebAndDateFin(dateDeb, dateFin);
    }

    public List<Contrat> getHistoriqueVehiculedeAgent(int agentId, int vehiculeId) {
        return contratRepository.findByAgentIdAndVehiculeId(agentId, vehiculeId);
    }

    public List<Contrat> getContratsPresignesPourAgent(int agentId) {
        return contratRepository.findByAgentIdAndStatut(agentId, Contrat.Statut.Presigne);
    }

    public List<Contrat> getContratsTerminesAccepteesParLoueur(int loueurId) {
        return contratRepository.findByLoueurIdAndStatutAndDateFinBefore(
                loueurId, Contrat.Statut.Accepte, new Date());
    }

    public List<Contrat> getContratsTerminesAccepteesParAgent(int agentId) {
        return contratRepository.findByAgentIdAndStatutAndDateFinBefore(
                agentId, Contrat.Statut.Accepte, new Date());
    }

    public Contrat getContratTermineAcceptePourLoueur(Long contratId, int loueurId) {
        return contratRepository
                .findByIdAndLoueurIdAndStatutAndDateFinBefore(
                        contratId, loueurId, Contrat.Statut.Accepte, new Date())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrat introuvable ou non autorisé (ou pas terminé/accepté)."));
    }

    public Contrat getContratTermineAcceptePourAgent(Long contratId, int agentId) {
        return contratRepository
                .findByIdAndAgentIdAndStatutAndDateFinBefore(
                        contratId, agentId, Contrat.Statut.Accepte, new Date())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrat introuvable ou non autorisé (ou pas terminé/accepté)."));
    }

    public Contrat getContratById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
    }

    public void supprimerContrat(Long id) {
        contratRepository.deleteById(id);
    }

    public Contrat save(Contrat c) {
        return contratRepository.save(c);
    }
}
