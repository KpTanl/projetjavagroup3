package com.group3.carrental.repository;

import com.group3.carrental.entity.PrestataireEntretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestataireEntretienRepository extends JpaRepository<PrestataireEntretien, String> {
}
