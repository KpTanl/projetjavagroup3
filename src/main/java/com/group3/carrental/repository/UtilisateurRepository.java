package com.group3.carrental.repository;

import com.group3.carrental.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilisateurRepository {
    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private List<String> localisations = Arrays.asList("abc", "defg");
    private List<Integer> notes = Arrays.asList(1, 2, 5);
    private LocalDate date = LocalDate.of(2025, 2, 2);



    public UtilisateurRepository() {
        utilisateurs.add(new Loueur(16, "Leclerc", "Charles", "charles.leclerc@example.com", "password", localisations, notes));
        utilisateurs.add(new Loueur(3, "Verstappen", "Max", "max.verstappen@example.com", "password", localisations, notes));
        utilisateurs.add(new Loueur(1, "Norris", "Lando", "lando.norris@example.com", "password", localisations, notes));

        utilisateurs.add(new AgentPro(4, "Agent", "Agent", "agent.agent@example.com", "password", notes, date, 0, null));
    }

    public List<Utilisateur> findAll() {
        return utilisateurs;
    }

    public Utilisateur findById(int id) {
        return utilisateurs.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public Utilisateur connecter(String email, String motDePasse) {
        return utilisateurs.stream()
                .filter(u -> u.getEmail().equals(email) && u.getMotDePasse().equals(motDePasse))
                .findFirst()
                .orElse(null);
    }

    public Utilisateur inscrire(String nom, String prenom, String email, String motDePasse, List<String> historiqueLocations, List<Integer> notesRecues) {
        boolean emailExiste = utilisateurs.stream().anyMatch(u -> u.getEmail().equals(email));
        if (emailExiste) {
            return null;
        }
        int newId = utilisateurs.stream().mapToInt(Utilisateur::getId).max().orElse(0) + 1;
        Utilisateur newUser = new Loueur(newId, nom, prenom, email, motDePasse, historiqueLocations, notesRecues);
        utilisateurs.add(newUser);
        return newUser;
    }
}