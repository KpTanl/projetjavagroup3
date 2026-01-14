package com.group3.carrental.repository;

import com.group3.carrental.entity.OptionPayanteAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionPayanteAgentRepository extends JpaRepository<OptionPayanteAgent, Long> {
}
