package com.group3.carrental.service;

import java.io.IOException;
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

    /**
     * Récupérer les contrats à rendre pour un loueur (date de fin passée, statut != Rendu)
     */
    public List<Contrat> getContratsARendre(int loueurId) {
        Date maintenant = new Date();
        return contratRepository.findByLoueurId(loueurId).stream()
            .filter(c -> c.getStatut() != Contrat.Statut.Rendu)
            .filter(c -> c.getDateFin().before(maintenant))
            .toList();
    }

    /**
     * Marquer un contrat comme rendu avec photo de kilométrage
     */
    public void rendreVehicule(Long contratId, String cheminPhoto) {
        Contrat contrat = getContratById(contratId);
        contrat.setStatut(Contrat.Statut.Rendu);
        contrat.setCheminPhotoKilometrage(cheminPhoto);
        contratRepository.save(contrat);
    }

    /**
     * Générer le PDF d'un contrat
     */
    public String genererPdfContrat(Long contratId, String dossierDestination) {
        try {
            Contrat contrat = getContratById(contratId);
            String cheminPdf = contrat.genererPdf(dossierDestination);
            System.out.println("PDF généré avec succès: " + cheminPdf);
            return cheminPdf;
        } catch (IOException e) {
            System.out.println("Erreur lors de la génération du PDF: " + e.getMessage());
            return null;
        }
    }
}
