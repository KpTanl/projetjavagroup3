package com.group3.carrental.repository;

import com.group3.carrental.entity.OptionPayanteAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.group3.carrental.entity.Agent;

@Repository
public interface OptionPayanteAgentRepository extends JpaRepository<OptionPayanteAgent, Long> {
    List<OptionPayanteAgent> findByAgent(Agent agent);
}
