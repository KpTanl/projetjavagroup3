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

    /**
     * Créer et sauvegarder un nouveau contrat
     */
    public Contrat creerContrat(Date dateDebut, Date dateFin, Agent agent, Loueur loueur, Vehicule vehicule, double prixTotal) {
        Contrat contrat = new Contrat(dateDebut, dateFin, agent, loueur, vehicule, prixTotal);
        return contratRepository.save(contrat);
    }

    /**
     * Récupérer tous les contrats
     */
    public List<Contrat> getTousLesContrats() {
        return contratRepository.findAll();
    }

    /**
     * Récupérer les contrats pour un loueur donné.
     */
    public List<Contrat> getContratsParLoueur(int loueurId) {
        return contratRepository.findByLoueurId(loueurId);
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
}
