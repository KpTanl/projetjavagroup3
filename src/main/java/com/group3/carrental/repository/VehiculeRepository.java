package com.group3.carrental.repository;

import com.group3.carrental.entity.Agent;
import com.group3.carrental.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {
    List<Vehicule> findByVilleLocalisation(String ville);

    List<Vehicule> findByEtat(Vehicule.EtatVehicule etat);

    List<Vehicule> findByAgent(Agent agent);

    Optional<Vehicule> findByMarqueAndModele(String marque, String modele);
}
