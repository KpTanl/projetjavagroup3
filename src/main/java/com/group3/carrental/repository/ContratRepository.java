package com.group3.carrental.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.carrental.entity.Contrat;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

    List<Contrat> findByLoueurId(int loueurId);

    List<Contrat> findByAgentId(int agentId);

    List<Contrat> findByVehiculeId(int vehiculeId);

    List<Contrat> findByAgentIdAndVehiculeId(int agentId, int vehiculeId);

    List<Contrat> findByDateDebAndDateFin(Date dateDeb, Date dateFin);

    List<Contrat> findByLoueurIdAndStatutAndDateFinBefore(int loueurId, Contrat.Statut statut, Date date);

    List<Contrat> findByAgentIdAndStatutAndDateFinBefore(int agentId, Contrat.Statut statut, Date date);

    List<Contrat> findByAgentIdAndStatut(int agentId, Contrat.Statut statut);

    Optional<Contrat> findByIdAndLoueurIdAndStatutAndDateFinBefore(Long id, int loueurId, Contrat.Statut statut,
            Date date);

    Optional<Contrat> findByIdAndAgentIdAndStatutAndDateFinBefore(Long id, int agentId, Contrat.Statut statut,
            Date date);
}
