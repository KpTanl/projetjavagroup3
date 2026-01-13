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

    // US.A.1 : Cette méthode permet de récupérer tous les Agents
    List<Utilisateur> findByRole(Utilisateur.Role role);
}