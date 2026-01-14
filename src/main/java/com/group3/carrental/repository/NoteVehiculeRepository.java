package com.group3.carrental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.NoteVehicule;

@Repository
public interface NoteVehiculeRepository extends JpaRepository<NoteVehicule, Long> {

    boolean existsByContrat_Id(Long contratId);
    Optional<NoteVehicule> findTopByContrat_IdOrderByIdDesc(Long contratId);
}
