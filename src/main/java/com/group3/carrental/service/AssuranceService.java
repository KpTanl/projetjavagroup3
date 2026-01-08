package com.group3.carrental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group3.carrental.entity.Assurance;
import com.group3.carrental.entity.Vehicule;
import com.group3.carrental.repository.AssuranceRepository;
import java.util.List;

@Service
public class AssuranceService {

    @Autowired
    private AssuranceRepository assuranceRepository;

    /**
     * Récupérer toutes les assurances disponibles
     */
    public List<Assurance> getAllAssurances() {
        return assuranceRepository.findAll();
    }

    /**
     * Récupérer une assurance par son ID
     */
    public Assurance getAssuranceById(Long id) {
        return assuranceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assurance non trouvée"));
    }

    /**
     * Calculer le prix de l'assurance pour un véhicule et une durée donnée
     * Utilise la grille tarifaire selon le type et modèle du véhicule
     */
    public double calculerPrix(Assurance assurance, Vehicule vehicule, int nbJours) {
        return assurance.calculerPrixTotal(vehicule, nbJours);
    }
    
    /**
     * Calculer le prix simple (sans véhicule spécifique)
     */
    public double calculerPrix(Assurance assurance, int nbJours) {
        return assurance.getPrixParJour() * nbJours;
    }
}
