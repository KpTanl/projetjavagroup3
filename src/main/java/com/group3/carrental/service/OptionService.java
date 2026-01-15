package com.group3.carrental.service;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.OptionPayanteAgent;
import com.group3.carrental.repository.OptionPayanteAgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    public static final String OPT_SIGNATURE_MANUELLE = "SIGNATURE_MANUELLE_CONTRAT";

    private final OptionPayanteAgentRepository optionRepository;

    public OptionService(OptionPayanteAgentRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public boolean hasActiveOption(int agentId, String type) {
        return optionRepository
                .findFirstByAgentIdAndTypeAndEstActiveTrue(agentId, type)
                .isPresent();
    }

    public List<OptionPayanteAgent> getOptionsByAgent(int agentId) {
        return optionRepository.findByAgentId(agentId);
    }

    public OptionPayanteAgent activerOption(Agent agent, String type, float prixMensuel) {
        OptionPayanteAgent opt = optionRepository
                .findFirstByAgentIdAndType(agent.getId(), type)
                .orElse(null);

        if (opt == null) {
            opt = new OptionPayanteAgent(type, prixMensuel, agent);
        }

        if (!opt.isEstActive()) {
            opt.souscrire();
        }

        return optionRepository.save(opt);
    }

    public OptionPayanteAgent desactiverOption(Agent agent, String type) {
        OptionPayanteAgent opt = optionRepository
                .findFirstByAgentIdAndType(agent.getId(), type)
                .orElseThrow(() -> new IllegalArgumentException("Option introuvable pour cet agent."));

        if (opt.isEstActive()) {
            opt.resilier();
        }

        return optionRepository.save(opt);
    }

    public OptionPayanteAgent toggleOption(Agent agent, String type, float prixMensuel) {
        boolean active = hasActiveOption(agent.getId(), type);
        if (active) {
            return desactiverOption(agent, type);
        }
        return activerOption(agent, type, prixMensuel);
    }

    /**
     * Souscrire à une nouvelle option payante.
     */
    public void souscrireNouvelleOption(Agent agent, String type, float prix) {
        OptionPayanteAgent nouvelleOption = new OptionPayanteAgent(type, prix, agent);
        nouvelleOption.souscrire();
        optionRepository.save(nouvelleOption);
    }

    /**
     * Annuler une option par son ID.
     */
    public void annulerOption(Long optionId) {
        OptionPayanteAgent opt = optionRepository.findById(optionId).orElse(null);
        if (opt != null) {
            opt.resilier();
            optionRepository.save(opt);
        }
    }

    /**
     * Récupérer les options par Agent 
     */
    public List<OptionPayanteAgent> getOptionsByAgent(Agent agent) {
        return optionRepository.findByAgentId(agent.getId());
    }
}
