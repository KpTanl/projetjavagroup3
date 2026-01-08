package com.group3.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group3.carrental.entity.Contrat;
import com.group3.carrental.entity.Loueur;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    
    // Méthodes de base héritées de JpaRepository suffisent pour l'instant
    // On peut ajouter des méthodes spécifiques plus tard si besoin
}
