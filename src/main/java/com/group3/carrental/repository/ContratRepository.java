package com.group3.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group3.carrental.entity.Contrat;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

}
