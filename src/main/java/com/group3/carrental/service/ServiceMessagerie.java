package com.group3.carrental.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group3.carrental.entity.Message;
import com.group3.carrental.entity.Utilisateur;
import com.group3.carrental.repository.MessageRepository;
import com.group3.carrental.repository.UtilisateurRepository;

@Service
public class ServiceMessagerie {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\d[-.\\s]?){8,14}\\d");

    public ServiceMessagerie(MessageRepository messageRepository, UtilisateurRepository utilisateurRepository) {
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public Message envoyerMessage(int expediteurId, int destinataireId, String contenu) {

        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du message ne peut pas être vide.");
        }
        if (expediteurId == destinataireId) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyer un message à vous-même.");
        }

        verifierMessage(contenu);

        Utilisateur expediteur = utilisateurRepository.findById(expediteurId)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable (id=" + expediteurId + ")"));

        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable (id=" + destinataireId + ")"));

        Message msg = new Message(expediteur, destinataire, contenu.trim());
        return messageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public List<Message> consulterMessages(int utilisateurId) {
        return messageRepository.findByDestinataire_IdOrderByDateEnvoiDesc(utilisateurId);
    }

    @Transactional(readOnly = true)
    public List<Message> consulterConversation(int utilisateur1Id, int utilisateur2Id) {
        if (utilisateur1Id == utilisateur2Id) {
            throw new IllegalArgumentException("Conversation invalide : utilisateur identique.");
        }
        return messageRepository.findConversation(utilisateur1Id, utilisateur2Id);
    }

    private void verifierMessage(String contenu) {
        String text = contenu.trim();

        if (EMAIL_PATTERN.matcher(text).find()) {
            throw new IllegalArgumentException("Échange d'adresse e-mail interdit via la messagerie.");
        }
        if (PHONE_PATTERN.matcher(text).find()) {
            throw new IllegalArgumentException("Échange de numéro de téléphone interdit via la messagerie.");
        }
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> rechercherUtilisateursParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        return utilisateurRepository.findByNomIgnoreCase(nom.trim());
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> rechercherUtilisateursParNomPrenom(String nom, String prenom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            return utilisateurRepository.findByNomIgnoreCase(nom.trim());
        }
        return utilisateurRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom.trim(), prenom.trim());
    }


}
