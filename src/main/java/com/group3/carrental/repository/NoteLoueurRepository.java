package com.group3.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.NoteLoueur;

@Repository
public interface NoteLoueurRepository extends JpaRepository<NoteLoueur, Long> {
    boolean existsByContrat_Id(Long contratId);
}
