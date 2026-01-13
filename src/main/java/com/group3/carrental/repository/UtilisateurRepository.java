package com.group3.carrental.repository;

import com.group3.carrental.entity.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByEmailAndMotDePasse(String email, String motDePasse);

    List<Utilisateur> findByNomIgnoreCase(String nom);
    List<Utilisateur> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}