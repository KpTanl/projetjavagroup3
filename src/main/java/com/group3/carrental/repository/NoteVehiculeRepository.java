package com.group3.carrental.repository;

import com.group3.carrental.entity.NoteVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteVehiculeRepository extends JpaRepository<NoteVehicule, String> {
}
