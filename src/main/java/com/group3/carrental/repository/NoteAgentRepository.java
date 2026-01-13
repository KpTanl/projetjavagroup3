package com.group3.carrental.repository;

import com.group3.carrental.entity.NoteAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteAgentRepository extends JpaRepository<NoteAgent, Long> {
}
