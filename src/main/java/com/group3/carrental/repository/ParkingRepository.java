package com.group3.carrental.repository;

import com.group3.carrental.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {

    // On utilise @Query pour dire explicitement à Spring quelle colonne utiliser
    // Attention : p.VilleP doit correspondre EXACTEMENT à l'orthographe dans ton entité
    @Query("SELECT p FROM Parking p WHERE p.VilleP = :ville")
    List<Parking> findByVilleP(@Param("ville") String ville);
}
