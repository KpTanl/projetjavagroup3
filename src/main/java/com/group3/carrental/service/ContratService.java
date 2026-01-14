package com.group3.carrental.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.ContratRepository;

@Service
public class ContratService {

    private final ContratRepository contratRepository;
    private final OptionService optionService;

    public ContratService(ContratRepository contratRepository, OptionService optionService) {
        this.contratRepository = contratRepository;
        this.optionService = optionService;
    }

    public Contrat creerContratPresigne(Date dateDeb, Date dateFin,
            Agent agent, Utilisateur loueur,
            Vehicule vehicule, double prixTotal) {

        Contrat contrat = new Contrat(dateDeb, dateFin, agent, loueur, vehicule, prixTotal);

        contrat.setStatut(Contrat.Statut.Presigne);

        boolean optionManuelle = optionService.hasActiveOption(
                agent.getId(),
                OptionService.OPT_SIGNATURE_MANUELLE);

        if (!optionManuelle) {
            contrat.setStatut(Contrat.Statut.Accepte);
        }

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

    /**
     * Récupérer les contrats à rendre pour un loueur (date de fin passée, statut !=
     * Rendu)
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

    public Contrat save(Contrat c) {
        return contratRepository.save(c);
    }
}
