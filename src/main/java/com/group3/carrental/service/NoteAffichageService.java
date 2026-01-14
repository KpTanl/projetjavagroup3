package com.group3.carrental.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.group3.carrental.entity.NoteAgent;
import com.group3.carrental.entity.NoteLoueur;
import com.group3.carrental.entity.NoteVehicule;
import com.group3.carrental.repository.NoteAgentRepository;
import com.group3.carrental.repository.NoteLoueurRepository;
import com.group3.carrental.repository.NoteVehiculeRepository;

@Service
public class NoteAffichageService {

    private final NoteVehiculeRepository noteVehiculeRepository;
    private final NoteAgentRepository noteAgentRepository;
    private final NoteLoueurRepository noteLoueurRepository;

    public NoteAffichageService(NoteVehiculeRepository noteVehiculeRepository,
                                NoteAgentRepository noteAgentRepository,
                                NoteLoueurRepository noteLoueurRepository) {
        this.noteVehiculeRepository = noteVehiculeRepository;
        this.noteAgentRepository = noteAgentRepository;
        this.noteLoueurRepository = noteLoueurRepository;
    }

    public Optional<NoteVehicule> getNoteVehiculeParContrat(Long contratId) {
        return noteVehiculeRepository.findTopByContrat_IdOrderByIdDesc(contratId);
    }

    public Optional<NoteAgent> getNoteAgentParContrat(Long contratId) {
        return noteAgentRepository.findTopByContrat_IdOrderByIdDesc(contratId);
    }

    public Optional<NoteLoueur> getNoteLoueurParContrat(Long contratId) {
        return noteLoueurRepository.findTopByContrat_IdOrderByIdDesc(contratId);
    }
}