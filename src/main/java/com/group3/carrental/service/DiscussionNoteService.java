package com.group3.carrental.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.DiscussionNoteMessage;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.entity.DiscussionNoteMessage.Cible;
import com.group3.carrental.repository.ContratRepository;
import com.group3.carrental.repository.DiscussionNoteMessageRepository;

@Service
public class DiscussionNoteService {

    private final DiscussionNoteMessageRepository discussionRepo;
    private final ContratRepository contratRepository;

    public DiscussionNoteService(DiscussionNoteMessageRepository discussionRepo,
                                 ContratRepository contratRepository) {
        this.discussionRepo = discussionRepo;
        this.contratRepository = contratRepository;
    }

    private Contrat getContratOrThrow(Long contratId) {
        return contratRepository.findById(contratId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat introuvable."));
    }

    private void verifierAcces(Contrat c, Utilisateur u) {
        boolean estLoueur = (c.getLoueur() != null && c.getLoueur().getId() == u.getId());
        boolean estAgent  = (c.getAgent() != null && c.getAgent().getId() == u.getId());

        if (!estLoueur && !estAgent) {
            throw new IllegalArgumentException("Accès refusé : vous n'êtes pas participant de ce contrat.");
        }
    }

    @Transactional(readOnly = true)
    public List<DiscussionNoteMessage> getDiscussion(Long contratId) {
        return discussionRepo.findByContrat_IdOrderByDateCreationAsc(contratId);
    }

    @Transactional(readOnly = true)
    public List<DiscussionNoteMessage> getDiscussionParCible(Long contratId, Cible cible) {
        return discussionRepo.findByContrat_IdAndCibleOrderByDateCreationAsc(contratId, cible);
    }

    @Transactional
    public DiscussionNoteMessage ajouterMessage(Long contratId, Utilisateur auteur, Cible cible, String contenu) {
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Message vide.");
        }

        Contrat c = getContratOrThrow(contratId);
        verifierAcces(c, auteur);

        DiscussionNoteMessage msg = new DiscussionNoteMessage(c, auteur, cible, contenu.trim());
        return discussionRepo.save(msg);
    }
}