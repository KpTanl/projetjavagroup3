package com.group3.carrental.service;

import org.springframework.stereotype.Service;

import com.group3.carrental.entity.*;
import com.group3.carrental.repository.*;

@Service
public class NoteService {

    private final ContratRepository contratRepository;
    private final NoteVehiculeRepository noteVehiculeRepository;
    private final NoteAgentRepository noteAgentRepository;
    private final NoteLoueurRepository noteLoueurRepository;

    public NoteService(ContratRepository contratRepository,
                       NoteVehiculeRepository noteVehiculeRepository,
                       NoteAgentRepository noteAgentRepository,
                       NoteLoueurRepository noteLoueurRepository) {
        this.contratRepository = contratRepository;
        this.noteVehiculeRepository = noteVehiculeRepository;
        this.noteAgentRepository = noteAgentRepository;
        this.noteLoueurRepository = noteLoueurRepository;
    }

    private Contrat chargerContratValide(Long contratId) {
        Contrat c = contratRepository.findById(contratId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat introuvable."));
        if (!c.estAccepte()) throw new IllegalArgumentException("Contrat non accepté.");
        if (!c.estTermine()) throw new IllegalArgumentException("Contrat non terminé (location en cours).");
        return c;
    }

    private void verifierNote1a5(int... notes) {
        for (int n : notes) {
            if (n < 1 || n > 5) throw new IllegalArgumentException("Les notes doivent être entre 1 et 5.");
        }
    }

    public NoteVehicule noterVehicule(Long contratId, Loueur loueur,
                                    int proprete, int usure, int confort,
                                    String commentaire) {

        Contrat c = chargerContratValide(contratId);

        if (c.getLoueur() == null || c.getLoueur().getId() != loueur.getId())
            throw new IllegalArgumentException("Ce contrat ne vous appartient pas.");

        if (noteVehiculeRepository.existsByContrat_Id(contratId))
            throw new IllegalArgumentException("Véhicule déjà noté pour ce contrat.");

        verifierNote1a5(proprete, usure, confort);

        NoteVehicule nv = new NoteVehicule(
                proprete,
                usure,
                confort,
                commentaire,
                c.getVehicule(),
                loueur,    
                c 
        );

        return noteVehiculeRepository.save(nv);
    }


    public NoteAgent noterAgent(Long contratId, Loueur auteur, int gestion, int bienveillance, int reactivite, String commentaire) {
        Contrat c = chargerContratValide(contratId);

        if (c.getLoueur() == null || c.getLoueur().getId() != auteur.getId())
            throw new IllegalArgumentException("Ce contrat ne vous appartient pas.");

        if (noteAgentRepository.existsByContrat_Id(contratId))
            throw new IllegalArgumentException("Agent déjà noté pour ce contrat.");

        verifierNote1a5(gestion, bienveillance, reactivite);

        NoteAgent na = new NoteAgent(gestion, bienveillance, reactivite, commentaire, c.getAgent(), auteur, c);

        return noteAgentRepository.save(na);
    }


    public NoteLoueur noterLoueur(Long contratId, Agent agent,
                                int traitement, int engagement, int responsabilite,
                                String commentaire) {

        Contrat c = chargerContratValide(contratId);

        if (c.getAgent() == null || c.getAgent().getId() != agent.getId())
            throw new IllegalArgumentException("Ce contrat ne vous appartient pas (côté agent).");

        if (noteLoueurRepository.existsByContrat_Id(contratId))
            throw new IllegalArgumentException("Loueur déjà noté pour ce contrat.");

        verifierNote1a5(traitement, engagement, responsabilite);

        NoteLoueur nl = new NoteLoueur(
                traitement,
                engagement,
                responsabilite,
                commentaire,
                c.getLoueur(),
                agent,
                c
        );

        return noteLoueurRepository.save(nl);
    }

}

