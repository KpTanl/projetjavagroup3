package com.group3.carrental.service;

import com.group3.carrental.repository.OptionPayanteAgentRepository;
import com.group3.carrental.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.carrental.entity.OptionPayanteAgent;
import com.group3.carrental.entity.Agent;

@Service
public class OptionService {
    @Autowired
    private OptionPayanteAgentRepository optionRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

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
}
