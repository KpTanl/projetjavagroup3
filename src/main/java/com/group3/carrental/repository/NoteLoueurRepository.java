package com.group3.carrental.repository;

import com.group3.carrental.entity.NoteLoueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteLoueurRepository extends JpaRepository<NoteLoueur, String> {
}
