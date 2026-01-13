package com.group3.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.NoteAgent;

@Repository
public interface NoteAgentRepository extends JpaRepository<NoteAgent, Long> {
    boolean existsByContrat_Id(Long contratId);
}
