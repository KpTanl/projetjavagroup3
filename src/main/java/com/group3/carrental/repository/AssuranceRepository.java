package com.group3.carrental.repository;

import com.group3.carrental.entity.Assurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssuranceRepository extends JpaRepository<Assurance, Long> {
}
