package com.group3.carrental.repository;

import com.group3.carrental.entity.OptionPayanteAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionPayanteAgentRepository extends JpaRepository<OptionPayanteAgent, Long> {

    Optional<OptionPayanteAgent> findFirstByAgentIdAndTypeAndEstActiveTrue(int agentId, String type);

    List<OptionPayanteAgent> findByAgentId(int agentId);

    Optional<OptionPayanteAgent> findFirstByAgentIdAndType(int agentId, String type);
}