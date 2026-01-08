package com.group3.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group3.carrental.entity.Assurance;
import java.util.Optional;

@Repository
public interface AssuranceRepository extends JpaRepository<Assurance, Long> {
    
    // Trouver une assurance par son nom
    Optional<Assurance> findByNom(String nom);
}
