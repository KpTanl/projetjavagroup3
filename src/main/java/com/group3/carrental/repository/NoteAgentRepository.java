package com.group3.carrental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.NoteAgent;

@Repository
public interface NoteAgentRepository extends JpaRepository<NoteAgent, Long> {
    Optional<NoteAgent> findByContrat_Id(Long contratId);
    boolean existsByContrat_Id(Long contratId);
}
