package com.group3.carrental.service;

import com.group3.carrental.repository.OptionPayanteAgentRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import com.group3.carrental.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.OptionPayanteAgent;
import com.group3.carrental.entity.PrestataireEntretien;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.entity.Agent;

@Service
public class OptionService {
    @Autowired
    private OptionPayanteAgentRepository optionRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private VehiculeRepository vehiculeRepository;

    public void souscrireNouvelleOption(Agent agent, String type, float prix) {
        // 1. Création de l'objet
        OptionPayanteAgent nouvelleOption = new OptionPayanteAgent(type, prix, agent);

        // 2. Appel de la méthode de l'interface
        nouvelleOption.souscrire();

        // 3. Sauvegarde
        optionRepository.save(nouvelleOption);
    }

    public void annulerOption(Long optionId) {
        OptionPayanteAgent opt = optionRepository.findById(optionId).orElse(null);
        if (opt != null) {
            opt.resilier();
            optionRepository.save(opt); // Met à jour le statut estActive à false
        }
    }

    public java.util.List<OptionPayanteAgent> getOptionsByAgent(Agent agent) {
        return optionRepository.findByAgent(agent);
    }

    // --- MÉTHODE 1 : L'ENTRETIEN PONCTUEL (Déclenché par le menu) ---
    public void commanderEntretien(Agent agent, Vehicule vehicule, PrestataireEntretien prestataire) {
        System.out.println(
                "\n[COMMANDE] Envoi du véhicule " + vehicule.getModele() + " chez " + prestataire.getNomSociete());

        // On change l'état du véhicule pour qu'il ne soit plus louable pendant
        // l'entretien
        vehicule.setEtat(Vehicule.EtatVehicule.En_Entretien);
        vehiculeRepository.save(vehicule);

        System.out.println(
                "[SERVICE] L'entreprise " + prestataire.getNomSociete() + " a bien reçu la demande d'entretien.");
    }

    // --- MÉTHODE 2 : L'ENTRETIEN AUTOMATIQUE (Appelée lors d'un retour de
    // location) ---
    public void verifierEtDeclencherNettoyageAuto(Vehicule v) {
        Agent proprietaire = v.getAgentProprietaire();
        if (proprietaire == null) {
            return;
        }

        // On vérifie si l'agent a souscrit à l'abonnement de nettoyage et s'il est
        // actif
        boolean aOptionActive = proprietaire.getOptionsSouscrites().stream()
                .anyMatch(o -> o.getType().equalsIgnoreCase("NETTOYAGE_PRO") && o.isEstActive());

        if (aOptionActive) {
            System.out.println("[AUTO] Option Nettoyage Active détectée pour " + proprietaire.getNom());
            System.out.println("[AUTO] Envoi automatique du véhicule " + v.getModele() + " en nettoyage.");
            v.setEtat(Vehicule.EtatVehicule.En_Entretien);
            vehiculeRepository.save(v);
        }
    }
}
